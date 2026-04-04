package com.mashiverse.mashit.ui.screens.mashup

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.DownloadType
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.mappers.fromEntities
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.states.MashupUiState
import com.mashiverse.mashit.data.states.intents.ActionsIntent
import com.mashiverse.mashit.data.states.intents.DialogIntent
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.data.states.utils.StackManager
import com.mashiverse.mashit.utils.color.helpers.toHexString
import com.mashiverse.mashit.utils.helpers.getRandomTraits
import com.mashiverse.mashit.utils.helpers.startImageDownload
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
    private val stackManager = StackManager<MashupDetails>()

    private val walletFlow = dataStoreRepo.walletFlow
    private val collectionFlow = collectionRepo.collectionFlow

    init {
        observeWallet()
        observeCollection()
    }

    // Observers
    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { prefs ->
                val wallet = prefs.wallet

                if (!wallet.isNullOrEmpty()) {
                    mashupUiState.value = mashupUiState.value.copy(wallet = wallet)
                    val initialMashup = collectionRepo.getMashup(wallet)
                    mashupUiState.value =
                        mashupUiState.value.copy(mashupDetails = initialMashup)

                    stackManager.clear()
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

    // State
    private fun recordState() {
        stackManager.record(mashupUiState.value.mashupDetails)
    }

    fun onUndo() {
        stackManager.undo(mashupUiState.value.mashupDetails)?.let { previous ->
            mashupUiState.value = mashupUiState.value.copy(
                mashupDetails = previous,
                colors = previous.colors
            )
        }
    }

    fun onRedo() {
        stackManager.redo(mashupUiState.value.mashupDetails)?.let { next ->
            mashupUiState.value = mashupUiState.value.copy(
                mashupDetails = next,
                colors = next.colors
            )
        }
    }

    // Mashup
    fun onReset() {
        recordState()
        mashupUiState.value = mashupUiState.value.copy(mashupDetails = MashupDetails())
    }

    fun onMashupUpdate(mashupTrait: MashupTrait) {
        val uiState = mashupUiState.value
        val mashupDetails = uiState.mashupDetails

        recordState()

        val trait = mashupTrait.trait
        val assets = mashupDetails.assets.toMutableList()

        val assetIndex = assets.indexOfFirst { it.type == trait.type }
        if (assetIndex != -1) {
            if (assets[assetIndex].url != trait.url) {
                assets[assetIndex] = trait
            } else {
                assets[assetIndex] = assets[assetIndex].copy(url = null)
            }
        }

        mashupUiState.value = uiState.copy(
            mashupDetails = mashupDetails.copy(
                assets = assets,
            )
        )
    }

    fun onRandom() {
        val uiState = mashupUiState.value
        if (uiState.nfts.isEmpty()) return

        recordState()

        val randomAssets = getRandomTraits(uiState.nfts)
        mashupUiState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                assets = randomAssets.map { it.trait },
            )
        )
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = mashupUiState.value
            if (uiState.wallet.isNullOrEmpty()) return@launch

            val res = mashitRepo.saveMashup(
                wallet = uiState.wallet,
                mashupDetails = uiState.mashupDetails
            )

            val dialogContent = if (res?.success == true) {
                DialogContent(
                    title = "Mashup Saved",
                    text = "Enjoy sharing it with friends"
                )
            } else {
                DialogContent(title = "Save Error", text = "Please try again later")
            }

            mashupUiState.value = uiState.copy(
                dialogContent = dialogContent
            )
        }
    }

    // Colors
    fun onColorsSave() {
        recordState()
        val uiState = mashupUiState.value

        mashupUiState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                colors = uiState.colors
            )
        )
    }

    fun onColorsReset() {
        mashupUiState.value = mashupUiState.value.copy(
            colors = mashupUiState.value.mashupDetails.colors
        )
    }

    fun onColorTypeSelect(colorType: ColorType) {
        mashupUiState.value = mashupUiState.value.copy(selectedColorType = colorType)
    }

    fun onColorChange(color: Color) {
        val uiState = mashupUiState.value

        val hex = "#" + color.toHexString()
        val currentColors = uiState.colors

        mashupUiState.value = uiState.copy(
            colors = when (uiState.selectedColorType) {
                ColorType.BASE -> currentColors.copy(base = hex)
                ColorType.EYES -> currentColors.copy(eyes = hex)
                ColorType.HAIR -> currentColors.copy(hair = hex)
            }
        )
    }

    // Category
    fun onCategorySelect(scope: CoroutineScope, state: LazyGridState, selectedCategory: TraitType) {
        mashupUiState.value = mashupUiState.value.copy(selectedCategory = selectedCategory)
        scope.launch { state.scrollToItem(0) }
    }

    // Images
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

    fun onImageSave(context: Context, downloadType: DownloadType) {
        mashupUiState.value.wallet?.let { wallet ->
            startImageDownload(wallet, downloadType.type, context)
        }
    }

    // Intents
    fun processActionsIntent(intent: ActionsIntent) {
        val uiState = mashupUiState.value

        when (intent) {
            is ActionsIntent.OnColor -> mashupUiState.value =
                uiState.copy(isColorChange = true)

            is ActionsIntent.OnColorDismiss -> mashupUiState.value =
                uiState.copy(isColorChange = false)

            is ActionsIntent.OnPreview -> mashupUiState.value =
                uiState.copy(isPreview = true)

            is ActionsIntent.OnPreviewDismiss -> mashupUiState.value =
                uiState.copy(isPreview = false)

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