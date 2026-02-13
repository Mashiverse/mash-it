package com.mashiverse.mashit.ui.screens.shop

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import com.mashiverse.mashit.utils.helpers.SoldHelper
import com.mashiverse.mashit.utils.helpers.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val mashItRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo,
    private val web3Repo: Web3Repo
) : ViewModel() {

    val shopPagingData: Flow<PagingData<Nft>> = mashItRepo.getShopListPagingData()
        .cachedIn(viewModelScope)

    private val _selectedId = mutableStateOf<String?>(null)
    private val _selectedNft = mutableStateOf<Nft?>(null)
    val selectedNft: State<Nft?> get() = _selectedNft

    fun selectId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedId.value = id
            _selectedNft.value = null
            try {
                _selectedNft.value = mashItRepo.getShopItem(id)
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch shop item details")
            }
        }
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

    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return web3Repo.getCoinbaseSdk(openIntent)
    }

    suspend fun getTotalSold(listing: Int): Int {
        return withContext(Dispatchers.IO) {
            try {
                SoldHelper.getTotalSold(listing.toLong()).toInt()
            } catch (e: Exception) {
                0
            }
        }
    }

    fun mint(client: CoinbaseWalletSDK, fromAddress: String, listingId: String, price: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val hasEnoughUsdc = Web3Helper.hasEnoughUsdc(address = fromAddress, price = price)

            if (hasEnoughUsdc) {
                Web3Helper.mint(
                    client,
                    fromAddress = fromAddress,
                    listingId = listingId,
                    price = price
                )
            } else {
                Timber.tag("GG").d("Not enough usdc to mint")
            }
        }
    }
}