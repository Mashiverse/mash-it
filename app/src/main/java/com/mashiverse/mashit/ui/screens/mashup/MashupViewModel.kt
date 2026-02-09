package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.color.ColorType
import com.mashiverse.mashit.data.models.color.SelectedColors
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val imageTypeRepo: ImageTypeRepo
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

    fun changeMashupTrait(trait: Trait) {
        val assets = (mashupDetails.value.assets).toMutableList()
        val assetToUpdate = assets.firstOrNull { it.type == trait.type }
        val i = assets.indexOf(assetToUpdate)

        if (assetToUpdate?.url != trait.url) {
            assets[i] = trait
        } else {
            assets[i] = assets[i].copy(url = null)
        }

        _mashupDetails.value = mashupDetails.value.copy(assets = assets)
    }

    fun changeMint(mint: Int? = null) {
        _mashupDetails.value = mashupDetails.value.copy(mint = mint)
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
}