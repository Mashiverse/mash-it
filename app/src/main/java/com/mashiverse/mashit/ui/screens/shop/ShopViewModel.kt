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
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.utils.helpers.web3.SoldHelper
import com.mashiverse.mashit.utils.helpers.web3.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    dataStoreRepo: DatastoreRepo,
    private val mashItRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo,
    private val web3Repo: Web3Repo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletFlow

    val shopPagingData: Flow<PagingData<Nft>> = mashItRepo.getShopListPagingData()
        .cachedIn(viewModelScope)

    private val _selectedId = mutableStateOf<String?>(null)
    private val _selectedNft = mutableStateOf<Nft?>(null)
    val selectedNft: State<Nft?> get() = _selectedNft

    private val _dialogContent = mutableStateOf<DialogContent?>(null)
    val dialogContent: State<DialogContent?> = _dialogContent

    fun clearDialog() {
        _dialogContent.value = null
    }

    fun getSearchPagingData(q: String): Flow<PagingData<Nft>> = mashItRepo
        .getSearchListPagingData(q = q)
        .cachedIn(viewModelScope)

    fun setDialogContent(dialogContent: DialogContent) {
        _dialogContent.value = dialogContent
    }

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

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.OnTypeGet -> getImageType(intent.url, intent.onResult)

            is ImageIntent.OnTypeSet -> setImageType(intent.url, intent.type)
        }
    }

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

    fun processWeb3Intent(intent: Web3Intent) {
        when (intent) {
            is Web3Intent.OnCoinbaseGet -> getCoinbaseSdk(
                openIntent = intent.onOpen
            )

            is Web3Intent.OnTotalSoldGet -> getTotalSold(
                listing = intent.listingId,
                callback = intent.callback
            )

            is Web3Intent.OnMint -> mint(
                listingId = intent.listingId,
                fromAddress = intent.from,
                price = intent.price,
                client = intent.client
            )
        }
    }

    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return web3Repo.getCoinbaseSdk(openIntent)
    }

    fun getTotalSold(listing: Int, callback: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalSold = SoldHelper.getTotalSold(listing.toLong()).toInt()
                callback.invoke(totalSold)
            } catch (e: Exception) {
                callback.invoke(0)
            }
        }
    }

    fun mint(client: CoinbaseWalletSDK, fromAddress: String, listingId: String, price: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            Web3Helper.mint(
                client,
                fromAddress = fromAddress,
                listingId = listingId,
                price = price,
                onMintFailure = { dialogContent ->
                    _dialogContent.value = dialogContent
                }
            )
        }
    }
}