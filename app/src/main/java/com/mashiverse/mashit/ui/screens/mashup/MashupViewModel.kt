package com.mashiverse.mashit.ui.screens.mashup

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
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
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.sys.workers.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayDeque

@HiltViewModel
class MashupViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {

    // --- State & History ---
    private val _mashupDetails = mutableStateOf(MashupDetails())
    val mashupDetails: State<MashupDetails> get() = _mashupDetails

    private val undoStack = ArrayDeque<MashupDetails>(10)
    private val redoStack = ArrayDeque<MashupDetails>(10)
    private val maxHistory = 10

    val canUndo = derivedStateOf { undoStack.isNotEmpty() }
    val canRedo = derivedStateOf { redoStack.isNotEmpty() }

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

    // --- Undo/Redo Core Logic ---

    private fun saveStateForUndo() {
        // Capture a snapshot of the state BEFORE the change occurs
        undoStack.addLast(_mashupDetails.value.copy())

        // Keep history at exactly 10
        if (undoStack.size > maxHistory) {
            undoStack.removeFirst()
        }

        // Every new manual action invalidates the "future" redo path
        redoStack.clear()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val currentState = _mashupDetails.value.copy()
            redoStack.addLast(currentState)

            _mashupDetails.value = undoStack.removeLast()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val currentState = _mashupDetails.value.copy()
            undoStack.addLast(currentState)

            _mashupDetails.value = redoStack.removeLast()
        }
    }

    // --- State Modifiers ---

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

    // --- Repository & Worker Actions ---

    fun getTraitTypeEntity(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    fun insertTraitType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = ImageTypeEntity(url, imageType)
            imageTypeRepo.insertImageType(entity)
        }
    }

    fun startImageUpload(wallet: String, imgType: Int, context: Context) {
        val inputData = Data.Builder()
            .putString(UploadWorker.WALLET, wallet)
            .putInt(UploadWorker.IMG_TYPE, imgType)
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