package com.mashiverse.mashit.ui.screens.shop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mashiverse.mashit.data.local.db.entities.TraitTypeEntity
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.ListingDetails
import com.mashiverse.mashit.data.models.mashi.MashiDetails
import com.mashiverse.mashit.data.repos.MashItRepo
import com.mashiverse.mashit.data.repos.TraitTypeRepo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val mashItRepo: MashItRepo,
    private val traitTypeRepo: TraitTypeRepo
): ViewModel() {
    private val _listings =  mutableStateOf<List<ListingDetails>>(listOf())
    val listings: State<List<ListingDetails>> get() = _listings

    private val _hasMore = mutableStateOf(false)
    val hasMore: State<Boolean> get() = _hasMore

    private val _selectedId = mutableStateOf<String?>(null)

    private val _selectedMashi = mutableStateOf<MashiDetails?>(null)
    val selectedMashi: State<MashiDetails?> get() = _selectedMashi

    init {
        fetchShopListings()
    }

    fun selectId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedId.value = id
            _selectedMashi.value = null

            _selectedMashi.value = mashItRepo.getShopItem(_selectedId.value!!)
        }
    }

    private fun fetchShopListings() {
        viewModelScope.launch(Dispatchers.IO) {
            val listingsDetails =  mashItRepo.getShopList()
            _listings.value = listingsDetails.listings
            _hasMore.value = listingsDetails.hasMore
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