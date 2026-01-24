package io.mashit.mashit.ui.screens.mashup

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.local.db.entities.TraitTypeEntity
import io.mashit.mashit.data.models.color.ColorType
import io.mashit.mashit.data.models.color.SelectedColors
import io.mashit.mashit.data.models.image.ImageType
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.MashupDetails
import io.mashit.mashit.data.repos.CollectionRepo
import io.mashit.mashit.data.repos.DataStoreRepo
import io.mashit.mashit.data.repos.MashiRepo
import io.mashit.mashit.data.repos.TraitTypeRepo
import io.mashit.mashit.utils.io.saveImageToGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DataStoreRepo,
    private val mashiRepo: MashiRepo,
    private val traitTypeRepo: TraitTypeRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.getCollectionFlow()

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
                        collectionRepo.updateData(prefs.wallet)
                        _mashupDetails.value = collectionRepo.getMashup(prefs.wallet)
                    }
                }
        }
    }

    fun changeMashupTrait(mashiTrait: MashiTrait) {
        val assets = (mashupDetails.value.assets ?: emptyList()).toMutableList()
        val assetToUpdate = assets.firstOrNull { it.traitType == mashiTrait.traitType }

        if (assetToUpdate != null) {
            assets.remove(assetToUpdate)
        }

        if (assetToUpdate?.url != mashiTrait.url) {
            assets.add(mashiTrait)
        } else {
            assets.add(MashiTrait(mashiTrait.traitType, ""))
        }

        _mashupDetails.value = mashupDetails.value.copy(assets = assets)
    }

    fun selectColorType(colorType: ColorType) {
        _selectedColorType.value = colorType
    }

    fun changeColors(selectedColors: SelectedColors) {
        _mashupDetails.value = mashupDetails.value.copy(colors = selectedColors)
    }

    fun saveMashup(
        ctx: Context,
        wallet: String = "0xae42edc0fc636d1214a6c5d829c37d778398f17b",
        isStatic: Boolean = true
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val mashupResult = mashiRepo.getMashup(wallet, isStatic)
            val timestamp = System.currentTimeMillis()
            val fileName = if (mashupResult.contentType == "image/png") {
                "mashup_$timestamp.png"
            } else {
                "mashup_$timestamp.gif"
            }

            saveImageToGallery(
                context = ctx,
                imageBytes = mashupResult.bytes,
                fileName = fileName,
                mimeType = mashupResult.contentType
            )

            withContext(Dispatchers.Main) {
                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getTraitTypeEntity(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = traitTypeRepo.getTraitTypeEntity(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    fun insertTraitType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = TraitTypeEntity(url, imageType)
            traitTypeRepo.insertTraitType(entity)
        }
    }
}