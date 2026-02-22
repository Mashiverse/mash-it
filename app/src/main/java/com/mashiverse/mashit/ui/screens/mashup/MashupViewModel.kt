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
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.sys.workers.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val imageTypeRepo: ImageTypeRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.collectionFlow

    private val _selectedColorType = mutableStateOf(ColorType.BASE)
    val selectedColorType: State<ColorType> get() = _selectedColorType

    //Mashup
    private val _mashupDetails = mutableStateOf(MashupDetails())
    val mashupDetails: State<MashupDetails> get() = _mashupDetails

    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletPreferences
                .distinctUntilChanged()
                .collect { prefs ->
                    if (prefs.wallet != null) {
                        collectionRepo.updateOwnedData(prefs.wallet)
                        _mashupDetails.value = collectionRepo.getMashup(prefs.wallet)
                    } else {
                        collectionRepo.clearOwned()
                    }
                }
        }
    }

    fun updateMashup(mashupTrait: MashupTrait, isRandom: Boolean) {
        val trait = mashupTrait.trait
        var mint = _mashupDetails.value.mint

        val assets = (mashupDetails.value.assets).toMutableList()
        val assetToUpdate = assets.firstOrNull { it.type == trait.type }
        val i = assets.indexOf(assetToUpdate)

        if (isRandom || assetToUpdate?.url != trait.url) {
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

        _mashupDetails.value = mashupDetails.value.copy(assets = assets, mint = mint)
    }

    fun selectColorType(colorType: ColorType) {
        _selectedColorType.value = colorType
    }

    fun changeColors(selectedColors: SelectedColors) {
        _mashupDetails.value = mashupDetails.value.copy(colors = selectedColors)
    }

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

    fun startImageUpload(wallet: String, imgType: Int) {
        Timber.tag("GG").d("Started")
        // 1. Create Data to pass to the Worker
        val inputData = Data.Builder()
            .putString(UploadWorker.WALLET, wallet)
            .putInt(UploadWorker.IMG_TYPE, imgType)
            .build()

        // 2. Define Constraints (Optional but recommended)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when online
            .build()

        // 3. Create the WorkRequest
        val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        // 4. Enqueue the work
        WorkManager.getInstance(context).enqueueUniqueWork(
            UUID.randomUUID().toString(), // Unique name to prevent duplicates
            ExistingWorkPolicy.REPLACE, // Replace if a new one is started
            uploadRequest
        )
    }
}