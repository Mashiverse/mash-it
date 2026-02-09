package com.mashiverse.mashit.ui.screens.shop

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import com.mashiverse.mashit.utils.helpers.SoldHelper
import com.mashiverse.mashit.utils.helpers.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import timber.log.Timber
import java.math.BigInteger


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val mashItRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo,
    private val web3Repo: Web3Repo
) : ViewModel() {
    private val _nfts = mutableStateOf<List<Nft>>(listOf())
    val listings: State<List<Nft>> get() = _nfts

    private val _hasMore = mutableStateOf(false)
    val hasMore: State<Boolean> get() = _hasMore

    private val _selectedId = mutableStateOf<String?>(null)

    private val _selectedNft = mutableStateOf<Nft?>(null)
    val selectedNft: State<Nft?> get() = _selectedNft

    init {
        fetchShopListings()
    }

    fun selectId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedId.value = id
            _selectedNft.value = null

            _selectedNft.value = mashItRepo.getShopItem(_selectedId.value!!)
        }
    }

    private fun fetchShopListings() {
        viewModelScope.launch(Dispatchers.IO) {
            val listingsDetails = mashItRepo.getShopList()
            _nfts.value = listingsDetails.listings
            _hasMore.value = listingsDetails.hasMore
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
            SoldHelper.getTotalSold(listing.toLong()).toInt()
        }
    }

    fun mint(client: CoinbaseWalletSDK) {
        Web3Helper.preAuthorizeUsdc(client)
    }
}