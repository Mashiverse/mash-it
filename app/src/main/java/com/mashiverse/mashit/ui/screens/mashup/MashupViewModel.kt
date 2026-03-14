package com.mashiverse.mashit.ui.screens.mashup

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.intents.DialogIntent
import com.mashiverse.mashit.data.models.intents.ImageIntent
import com.mashiverse.mashit.data.models.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.states.MashupUiState
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.sys.workers.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val mashitRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {
    var mashupUiState = mutableStateOf(MashupUiState())
        private set



    private val undoStack = ArrayDeque<MashupDetails>(5)
    private val redoStack = ArrayDeque<MashupDetails>(5)
    private val maxHistory = 5



    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.collectionFlow

    private val _selectedColorType = mutableStateOf(ColorType.BASE)
    val selectedColorType: State<ColorType> get() = _selectedColorType

    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletPreferences
                .distinctUntilChanged()
                .collect { prefs ->
                    if (prefs.wallet != null) {
                        collectionRepo.updateOwnedData(prefs.wallet)
                        val initialMashup = collectionRepo.getMashup(prefs.wallet)
                        withContext(Dispatchers.Main) {
                            _mashupDetails.value = initialMashup
                            // Reset history when the source data changes (new wallet)
                            undoStack.clear()
                            redoStack.clear()
                        }
                    } else {
                        collectionRepo.clearOwned()
                    }
                }
        }
    }

    fun processMashupIntent(intent: MashupIntent) {
        when (intent) {
            is MashupIntent.OnColor -> {
                mashupUiState.value = mashupUiState.value.copy(isColorChange = true)
            }

            is MashupIntent.OnColorDismiss -> {
                mashupUiState.value = mashupUiState.value.copy(isColorChange = false)
            }

            is MashupIntent.OnPreview -> {
                mashupUiState.value = mashupUiState.value.copy(isPreview = true)
            }

            is MashupIntent.OnPreviewDismiss -> {
                mashupUiState.value = mashupUiState.value.copy(isPreview = false)
            }

            is MashupIntent.OnGifSave -> saveGif(intent.context)

            is MashupIntent.OnPngSave -> savePng(intent.context)
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
        return !_mashupDetails.value.assets.all { it.url == null }
    }

    private fun saveStateForUndo() {
        // ONLY save to history if the current screen isn't empty
        if (hasAnyTrait()) {
            undoStack.addLast(_mashupDetails.value.copy())
            if (undoStack.size > maxHistory) {
                undoStack.removeFirst()
            }
            redoStack.clear()
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val currentState = _mashupDetails.value.copy()
            // Only push to redo if current state has data
            if (hasAnyTrait()) {
                redoStack.addLast(currentState)
            }
            _mashupDetails.value = undoStack.removeLast()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val currentState = _mashupDetails.value.copy()
            // Only push to undo if current state has data
            if (hasAnyTrait()) {
                undoStack.addLast(currentState)
            }
            _mashupDetails.value = redoStack.removeLast()
        }
    }

    // --- State Modifiers ---

    fun reset() {
        saveStateForUndo()
        _mashupDetails.value = MashupDetails()
    }

    fun updateMashup(mashupTrait: MashupTrait) {
        saveStateForUndo()

        val trait = mashupTrait.trait
        var mint = _mashupDetails.value.mint

        val assets = (_mashupDetails.value.assets).toMutableList()
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

        _mashupDetails.value = _mashupDetails.value.copy(assets = assets, mint = mint)
    }

    fun saveMashup(wallet: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = mashitRepo.saveMashup(wallet = wallet, mashupDetails = _mashupDetails.value)
            if (res?.success == true) {
                _dialogContent.value = DialogContent(
                    title = "Mashup Saved",
                    text = "Enjoy sharing it with friends"
                )
            } else {
                _dialogContent.value = DialogContent(
                    title = "Save Error",
                    text = "Please try again later"
                )
            }
        }
    }

    fun randomizeMashup(mashupTraits: List<MashupTrait>) {
        saveStateForUndo()

        val mint = mashupTraits.firstOrNull { it.trait.type == TraitType.BACKGROUND }?.mint
        val traits = mashupTraits.map { it.trait }

        _mashupDetails.value = _mashupDetails.value.copy(assets = traits, mint = mint)
    }

    fun changeColors(selectedColors: SelectedColors) {
        saveStateForUndo()
        _mashupDetails.value = _mashupDetails.value.copy(colors = selectedColors)
    }

    fun selectColorType(colorType: ColorType) {
        _selectedColorType.value = colorType
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

    fun savePng(context: Context) {
        mashupUiState.value.wallet?.let { wallet ->
            startImageDownload(
                wallet = wallet,
                imageType = 0,
                context = context
            )
        }
    }

    fun saveGif(context: Context) {
        mashupUiState.value.wallet?.let { wallet ->
            startImageDownload(
                wallet = wallet,
                imageType = 1,
                context = context
            )
        }
    }

    fun startImageDownload(wallet: String, imageType: Int, context: Context) {
        val inputData = Data.Builder()
            .putString(UploadWorker.WALLET, wallet)
            .putInt(UploadWorker.IMG_TYPE, imageType)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UUID.randomUUID().toString(),
            ExistingWorkPolicy.REPLACE,
            uploadRequest
        )
    }
}