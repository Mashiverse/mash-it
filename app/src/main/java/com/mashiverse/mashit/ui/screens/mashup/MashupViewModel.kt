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


    private val undoStack = ArrayDeque<MashupDetails>(5)
    private val redoStack = ArrayDeque<MashupDetails>(5)
    private val maxHistory = 5

    init {
        observeWallet()
        observeCollection()
    }

    fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow
                .distinctUntilChanged()
                .collect { prefs ->
                    prefs.wallet?.let { wallet ->
                        mashupUiState.value = mashupUiState.value.copy(wallet = wallet)
                        collectionRepo.updateOwnedData(wallet)

                        val initialMashup = collectionRepo.getMashup(wallet)
                        withContext(Dispatchers.Main) {
                            mashupUiState.value =
                                mashupUiState.value.copy(mashupDetails = initialMashup)
                            // Reset history when the source data changes (new wallet)
                            undoStack.clear()
                            redoStack.clear()
                        }
                    }

                    if (prefs.wallet.isNullOrEmpty()) {
                        collectionRepo.clearOwned()
                    }
                }
        }
    }

    fun observeCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionFlow
                .distinctUntilChanged()
                .collect { collection ->
                    mashupUiState.value = mashupUiState.value.copy(nfts = collection.fromEntities())
                }
        }
    }


    fun processActionsIntent(intent: ActionsIntent) {
        when (intent) {
            is ActionsIntent.OnColor -> {
                mashupUiState.value = mashupUiState.value.copy(isColorChange = true)
            }

            is ActionsIntent.OnColorDismiss -> {
                mashupUiState.value = mashupUiState.value.copy(isColorChange = false)
            }

            is ActionsIntent.OnPreview -> {
                mashupUiState.value = mashupUiState.value.copy(isPreview = true)
            }

            is ActionsIntent.OnPreviewDismiss -> {
                mashupUiState.value = mashupUiState.value.copy(isPreview = false)
            }

            is ActionsIntent.OnImageSave -> onImageSave(intent.context, intent.downloadType)


            is ActionsIntent.OnRandom -> onRandom()

            is ActionsIntent.OnSave -> onSave()

            is ActionsIntent.OnReset -> onReset()

            ActionsIntent.OnRedo -> onRedo()

            ActionsIntent.OnUndo -> onUndo()
        }
    }


    fun onRandom() {
        if (mashupUiState.value.nfts.isNotEmpty()) {
            val randomAssets = TraitType.entries.mapNotNull { type ->
                when (type) {
                    TraitType.BACKGROUND, TraitType.EYES, TraitType.BOTTOM, TraitType.UPPER, TraitType.HEAD -> {
                        TraitsHelper.getTraitsByType(mashupUiState.value.nfts)[type]?.randomOrNull()
                    }

                    else -> {
                        val isIncluded = arrayOf(true, false).random()
                        if (isIncluded) {
                            TraitsHelper.getTraitsByType(mashupUiState.value.nfts)[type]?.randomOrNull()
                        } else {
                            MashupTrait(
                                trait = Trait(type = type, url = null),
                                avatarName = ""
                            )
                        }
                    }
                }
            }
            randomizeMashup(randomAssets)
        }
    }

    fun processMashupIntent(intent: MashupIntent) {
        when (intent) {
            is MashupIntent.OnCategorySelect -> {
                onCategorySelect(
                    scope = intent.scope,
                    state = intent.state,
                    selectedCategory = intent.selectedCategory
                )
            }

            is MashupIntent.OnColorChange -> onColorChange(intent.color)
            is MashupIntent.OnColorsReset -> onColorsReset()
            is MashupIntent.OnMashupUpdate -> onMashupUpdate(intent.trait)
            is MashupIntent.OnColorsSave -> onColorsSave()
            is MashupIntent.OnColorTypeSelect -> onColorTypeSelect(intent.colorType)
        }
    }

    fun onCategorySelect(
        scope: CoroutineScope,
        state: LazyGridState,
        selectedCategory: TraitType,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            mashupUiState.value = mashupUiState.value.copy(selectedCategory = selectedCategory)
            scope.launch { state.scrollToItem(0) }
        }
    }


    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.GetImageType -> getImageType(intent.url, intent.onResult)

            is ImageIntent.SetImageType -> setImageType(intent.url, intent.imageType)
        }
    }

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.Clear -> {
                mashupUiState.value = mashupUiState.value.copy(dialogContent = null)
            }

            is DialogIntent.SetContent -> {
                mashupUiState.value = mashupUiState.value.copy(dialogContent = intent.content)
            }
        }
    }


    // --- Undo/Redo Core Logic ---
    fun hasAnyTrait(): Boolean {
        return !mashupUiState.value.mashupDetails.assets.all { asset -> asset.url == null }
    }

    private fun saveStateForUndo() {
        // ONLY save to history if the current screen isn't empty
        if (hasAnyTrait()) {
            undoStack.addLast(mashupUiState.value.mashupDetails.copy())
            if (undoStack.size > maxHistory) {
                undoStack.removeFirst()
            }
            redoStack.clear()
        }
    }

    fun onUndo() {
        if (undoStack.isNotEmpty()) {
            val currentState = mashupUiState.value.mashupDetails.copy()
            // Only push to redo if current state has data
            if (hasAnyTrait()) {
                redoStack.addLast(currentState)
            }
            mashupUiState.value =
                mashupUiState.value.copy(mashupDetails = undoStack.removeLast())
        }
    }

    fun onRedo() {
        if (redoStack.isNotEmpty()) {
            val currentState = mashupUiState.value.mashupDetails.copy()
            // Only push to undo if current state has data
            if (hasAnyTrait()) {
                undoStack.addLast(currentState)
            }
            mashupUiState.value =
                mashupUiState.value.copy(mashupDetails = redoStack.removeLast())
        }
    }

// --- State Modifiers ---

    fun onReset() {
        saveStateForUndo()
        mashupUiState.value = mashupUiState.value.copy(mashupDetails = MashupDetails())
    }

    fun onMashupUpdate(mashupTrait: MashupTrait) {
        saveStateForUndo()

        val trait = mashupTrait.trait
        var mint = mashupUiState.value.mashupDetails.mint

        val assets = (mashupUiState.value.mashupDetails.assets).toMutableList()
        val assetToUpdate = assets.firstOrNull { it.type == trait.type }
        val i = assets.indexOf(assetToUpdate)

        if (assetToUpdate?.url != trait.url) {
            assets[i] = trait
            if (assets[i].type == TraitType.BACKGROUND) {
                mint = mashupTrait.mint
            }
        } else if (mint != mashupTrait.mint) {
            mint = mashupTrait.mint
        } else {
            assets[i] = assets[i].copy(url = null)
            if (assets[i].type == TraitType.BACKGROUND) {
                mint = null
            }
        }

        mashupUiState.value =
            mashupUiState.value.copy(
                mashupDetails = mashupUiState.value.mashupDetails.copy(
                    assets = assets,
                    mint = mint
                )
            )
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = mashupUiState.value.let { uiState ->
                if (uiState.wallet.isNullOrEmpty())
                    return@launch

                mashitRepo.saveMashup(
                    wallet = uiState.wallet,
                    mashupDetails = uiState.mashupDetails
                )
            }

            mashupUiState.value = mashupUiState.value.copy(
                dialogContent = when {
                    res?.success == true -> DialogContent(
                        title = "Mashup Saved",
                        text = "Enjoy sharing it with friends"
                    )

                    else -> DialogContent(
                        title = "Save Error",
                        text = "Please try again later"
                    )
                }
            )
        }
    }

    fun randomizeMashup(mashupTraits: List<MashupTrait>) {
        saveStateForUndo()

        val mint = mashupTraits.firstOrNull { it.trait.type == TraitType.BACKGROUND }?.mint
        val traits = mashupTraits.map { it.trait }

        mashupUiState.value = mashupUiState.value.copy(
            mashupDetails = mashupUiState.value.mashupDetails.copy(assets = traits, mint = mint)
        )
    }


    fun onColorTypeSelect(colorType: ColorType) {
        mashupUiState.value = mashupUiState.value.copy(selectedColorType = colorType)
    }

    // Colors

    fun onColorChange(color: Color) {
        saveStateForUndo()
        val hex = "#" + color.toHexString()
        mashupUiState.value =
            mashupUiState.value.copy(
                colors =
                    when (mashupUiState.value.selectedColorType) {
                        ColorType.BASE -> mashupUiState.value.colors.copy(base = hex)
                        ColorType.EYES -> mashupUiState.value.colors.copy(eyes = hex)
                        ColorType.HAIR -> mashupUiState.value.colors.copy(hair = hex)
                    }
            )
    }

    fun onColorsSave() {
        saveStateForUndo()
        mashupUiState.value = mashupUiState.value.copy(
            mashupDetails = mashupUiState.value.mashupDetails.copy(
                colors = mashupUiState.value.colors
            )
        )
    }

    fun onColorsReset() {
        saveStateForUndo()
        mashupUiState.value = mashupUiState.value.copy(
            colors = mashupUiState.value.mashupDetails.colors
        )
    }

    // Image type

    fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = ImageTypeEntity(url, imageType)
            imageTypeRepo.insertImageType(entity)
        }
    }

    // Download

    fun onImageSave(context: Context, downloadType: DownloadImageType) {
        mashupUiState.value.wallet?.let { wallet ->
            DownloadHelper.startImageDownload(
                wallet = wallet,
                imageType = downloadType.type,
                context = context
            )
        }
    }
}