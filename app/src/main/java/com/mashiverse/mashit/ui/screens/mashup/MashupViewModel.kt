package com.mashiverse.mashit.ui.screens.mashup

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashiverse.mashit.data.intents.ActionsIntent
import com.mashiverse.mashit.data.intents.DialogIntent
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.MashupIntent
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.DownloadImageType
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.mappers.fromEntities
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.states.MashupUiState
import com.mashiverse.mashit.utils.color.helpers.toHexString
import com.mashiverse.mashit.utils.helpers.DownloadHelper
import com.mashiverse.mashit.utils.helpers.TraitsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    val collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val mashitRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {
    var mashupUiState = mutableStateOf(MashupUiState())
        private set

    val walletFlow = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.collectionFlow


    private val undoStack = ArrayDeque<MashupDetails>()
    private val redoStack = ArrayDeque<MashupDetails>()
    private val maxHistory = 10

    init {
        observeWallet()
        observeCollection()
    }

    // --- Intent Processing ---

    fun processActionsIntent(intent: ActionsIntent) {
        when (intent) {
            is ActionsIntent.OnColor -> mashupUiState.value =
                mashupUiState.value.copy(isColorChange = true)

            is ActionsIntent.OnColorDismiss -> mashupUiState.value =
                mashupUiState.value.copy(isColorChange = false)

            is ActionsIntent.OnPreview -> mashupUiState.value =
                mashupUiState.value.copy(isPreview = true)

            is ActionsIntent.OnPreviewDismiss -> mashupUiState.value =
                mashupUiState.value.copy(isPreview = false)

            is ActionsIntent.OnImageSave -> onImageSave(intent.context, intent.downloadType)
            is ActionsIntent.OnRandom -> onRandom()
            is ActionsIntent.OnSave -> onSave()
            is ActionsIntent.OnReset -> onReset()
            is ActionsIntent.OnRedo -> onRedo()
            is ActionsIntent.OnUndo -> onUndo()
        }
    }

    fun processMashupIntent(intent: MashupIntent) {
        when (intent) {
            is MashupIntent.OnCategorySelect -> onCategorySelect(
                intent.scope,
                intent.state,
                intent.selectedCategory
            )

            is MashupIntent.OnColorChange -> onColorChange(intent.color)
            is MashupIntent.OnColorsReset -> onColorsReset()
            is MashupIntent.OnMashupUpdate -> onMashupUpdate(intent.trait)
            is MashupIntent.OnColorsSave -> onColorsSave()
            is MashupIntent.OnColorTypeSelect -> onColorTypeSelect(intent.colorType)
        }
    }

    private fun recordStateForUndo() {

        val currentState = mashupUiState.value.mashupDetails.copy()

        undoStack.addLast(currentState)

        if (undoStack.size > maxHistory) {
            undoStack.removeFirst()
        }
        // New user actions always invalidate the redo history
        redoStack.clear()
    }

    fun onUndo() {
        if (undoStack.isNotEmpty()) {
            // Save current state to Redo before moving back
            redoStack.addLast(mashupUiState.value.mashupDetails.copy())

            val previousState = undoStack.removeLast()
            mashupUiState.value = mashupUiState.value.copy(
                mashupDetails = previousState,
                colors = previousState.colors
            )
        }
    }

    fun onRedo() {
        if (redoStack.isNotEmpty()) {
            // Save current state to Undo before moving forward
            undoStack.addLast(mashupUiState.value.mashupDetails.copy())

            val nextState = redoStack.removeLast()
            mashupUiState.value =
                mashupUiState.value.copy(mashupDetails = nextState, colors = nextState.colors)
        }
    }

    // --- State Modifiers ---

    fun onReset() {
        recordStateForUndo()
        mashupUiState.value = mashupUiState.value.copy(mashupDetails = MashupDetails())
    }

    fun onMashupUpdate(mashupTrait: MashupTrait) {
        recordStateForUndo()

        val trait = mashupTrait.trait
        var mint = mashupUiState.value.mashupDetails.mint
        val assets = (mashupUiState.value.mashupDetails.assets).toMutableList()
        val assetIndex = assets.indexOfFirst { it.type == trait.type }

        if (assetIndex != -1) {
            if (assets[assetIndex].url != trait.url) {
                assets[assetIndex] = trait
                if (trait.type == TraitType.BACKGROUND) mint = mashupTrait.mint
            } else {
                assets[assetIndex] = assets[assetIndex].copy(url = null)
                if (trait.type == TraitType.BACKGROUND) mint = null
            }
        }

        mashupUiState.value = mashupUiState.value.copy(
            mashupDetails = mashupUiState.value.mashupDetails.copy(
                assets = assets,
                mint = mint
            )
        )
    }

    fun onRandom() {
        if (mashupUiState.value.nfts.isNotEmpty()) {
            recordStateForUndo()

            val randomAssets = TraitType.entries.map { type ->
                val available = TraitsHelper.getTraitsByType(mashupUiState.value.nfts)[type]
                if (type in listOf(
                        TraitType.BACKGROUND,
                        TraitType.EYES,
                        TraitType.BOTTOM,
                        TraitType.UPPER,
                        TraitType.HEAD
                    )
                ) {
                    available?.randomOrNull() ?: MashupTrait(Trait(type, null), "")
                } else {
                    if ((0..1).random() == 1) available?.randomOrNull() ?: MashupTrait(
                        Trait(
                            type,
                            null
                        ), ""
                    )
                    else MashupTrait(Trait(type, null), "")
                }
            }

            val mint = randomAssets.firstOrNull { it.trait.type == TraitType.BACKGROUND }?.mint
            mashupUiState.value = mashupUiState.value.copy(
                mashupDetails = mashupUiState.value.mashupDetails.copy(
                    assets = randomAssets.map { it.trait },
                    mint = mint
                )
            )
        }
    }

    fun onColorChange(color: Color) {
        val hex = "#" + color.toHexString()
        val currentColors = mashupUiState.value.colors

        mashupUiState.value = mashupUiState.value.copy(
            colors = when (mashupUiState.value.selectedColorType) {
                ColorType.BASE -> currentColors.copy(base = hex)
                ColorType.EYES -> currentColors.copy(eyes = hex)
                ColorType.HAIR -> currentColors.copy(hair = hex)
            }
        )
    }

    fun onColorsSave() {
        recordStateForUndo()
        mashupUiState.value = mashupUiState.value.copy(
            mashupDetails = mashupUiState.value.mashupDetails.copy(
                colors = mashupUiState.value.colors
            )
        )
    }

    fun onColorsReset() {
        mashupUiState.value = mashupUiState.value.copy(
            colors = mashupUiState.value.mashupDetails.colors
        )
    }

    // --- Repository & Flow Observers ---

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { prefs ->
                val wallet = prefs.wallet

                if (!wallet.isNullOrEmpty()) {
                    mashupUiState.value = mashupUiState.value.copy(wallet = wallet)
                    val initialMashup = collectionRepo.getMashup(wallet)
                    mashupUiState.value =
                        mashupUiState.value.copy(mashupDetails = initialMashup)
                    undoStack.clear()
                    redoStack.clear()
                    collectionRepo.updateOwnedData(wallet)
                } else {
                    mashupUiState.value = mashupUiState.value.copy(wallet = null)
                    collectionRepo.clearOwned()
                }
            }
        }
    }

    private fun observeCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionFlow.distinctUntilChanged().collect { collection ->
                mashupUiState.value = mashupUiState.value.copy(nfts = collection.fromEntities())
            }
        }
    }

    // --- Helper UI Methods ---

    fun onCategorySelect(scope: CoroutineScope, state: LazyGridState, selectedCategory: TraitType) {
        mashupUiState.value = mashupUiState.value.copy(selectedCategory = selectedCategory)
        scope.launch { state.scrollToItem(0) }
    }

    fun onColorTypeSelect(colorType: ColorType) {
        mashupUiState.value = mashupUiState.value.copy(selectedColorType = colorType)
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = mashupUiState.value
            if (uiState.wallet.isNullOrEmpty()) return@launch

            val res = mashitRepo.saveMashup(
                wallet = uiState.wallet,
                mashupDetails = uiState.mashupDetails
            )

            mashupUiState.value = mashupUiState.value.copy(
                dialogContent = if (res?.success == true) {
                    DialogContent(
                        title = "Mashup Saved",
                        text = "Enjoy sharing it with friends"
                    )
                } else {
                    DialogContent(title = "Save Error", text = "Please try again later")
                }
            )
        }
    }

    fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            imageTypeRepo.insertImageType(ImageTypeEntity(url, imageType))
        }
    }

    fun onImageSave(context: Context, downloadType: DownloadImageType) {
        mashupUiState.value.wallet?.let { wallet ->
            DownloadHelper.startImageDownload(wallet, downloadType.type, context)
        }
    }

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.Clear -> mashupUiState.value =
                mashupUiState.value.copy(dialogContent = null)

            is DialogIntent.SetContent -> mashupUiState.value =
                mashupUiState.value.copy(dialogContent = intent.content)
        }
    }

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.GetImageType -> getImageType(intent.url, intent.onResult)
            is ImageIntent.SetImageType -> setImageType(intent.url, intent.imageType)
        }
    }
}