package io.mashit.mashit.ui.screens.shop

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.models.mashi.ListingDetails
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.repos.MashItRepo
import io.mashit.mashit.data.repos.Web3Repo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val mashItRepo: MashItRepo
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
}