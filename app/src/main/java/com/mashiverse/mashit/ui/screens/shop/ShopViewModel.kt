package com.mashiverse.mashit.ui.screens.shop

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.DialogIntent
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.ShopDataType
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import com.mashiverse.mashit.data.states.ShopUiState
import com.mashiverse.mashit.utils.helpers.web3.SoldHelper
import com.mashiverse.mashit.utils.helpers.web3.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
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
    var shopUiState = mutableStateOf(ShopUiState())
        private set

    val walletFlow = dataStoreRepo.walletFlow

    init {
        observeWallet()
    }

    // Observers

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { prefs ->
                val wallet = prefs.wallet

                if (!wallet.isNullOrEmpty()) {
                    shopUiState.value = shopUiState.value.copy(wallet = wallet)
                } else {
                    shopUiState.value = shopUiState.value.copy(wallet = null)
                }
            }
        }
    }

    // Shop Intent

    fun processShopIntent(intent: ShopIntent) {
        when (intent) {
            is ShopIntent.OnDataTypeSelect -> {
                onDataTypeSelect(
                    dataType = intent.dataType,
                    query = intent.query
                )
            }

            is ShopIntent.OnCategorySelect -> onCategorySelect()

            is ShopIntent.OnCategoryClose -> onCategoryClose()

            is ShopIntent.OnNftSelect -> selectNft(intent.id)

            is ShopIntent.OnNftDeselect -> deselectNft()
        }
    }

    private fun onCategorySelect() {
        shopUiState.value = shopUiState.value.copy(isCategory = true)
    }

    private fun onDataTypeSelect(dataType: ShopDataType, query: String) {
        val itemsData = when (dataType) {
            ShopDataType.RECENT -> mashItRepo.getShopListPagingData()
                .cachedIn(viewModelScope)

            ShopDataType.SEARCH -> {
                if (query.isEmpty()) {
                    flowOf(PagingData.empty())
                } else {
                    mashItRepo
                        .getSearchListPagingData(q = query)
                        .cachedIn(viewModelScope)
                }
            }
        }

        shopUiState.value = shopUiState.value.copy(
            category = dataType,
            itemsData = itemsData,
        )
    }

    private fun onCategoryClose() {
        shopUiState.value = shopUiState.value.copy(isCategory = false)
    }


    private fun selectNft(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shopUiState.value = shopUiState.value.copy(
                    selectedNft = mashItRepo.getShopItem(id),
                    isExpanded = true
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch shop item details")
            }
        }
    }

    private fun deselectNft() {
        shopUiState.value = shopUiState.value.copy(
            selectedNft = null,
            isExpanded = false
        )
    }

    // Image Type

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

    // Dialog

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.OnClear -> shopUiState.value =
                shopUiState.value.copy(dialogContent = null)

            is DialogIntent.OnChange -> shopUiState.value =
                shopUiState.value.copy(dialogContent = intent.content)
        }
    }

    // Web3

    fun processWeb3Intent(intent: Web3Intent) {
        when (intent) {
            is Web3Intent.OnTotalSoldGet -> getTotalSold(
                listing = intent.listingId,
                callback = intent.callback
            )

            is Web3Intent.OnMint -> onMint(
                clientRef = intent.client,
                listingId = intent.listingId,
                price = intent.price,
                isPolCurrency = intent.isPolCurrency
            )
        }
    }

    private fun onMint(
        clientRef: CoinbaseWalletSDK,
        listingId: String,
        price: Double,
        isPolCurrency: Boolean
    ) {
        if (isPolCurrency) {
            shopUiState.value = shopUiState.value.copy(
                dialogContent = DialogContent(
                    title = "POL Currency",
                    text = "We currently don't support POL minting"
                )
            )
        } else if (shopUiState.value.wallet != null) {
            mint(
                client = clientRef,
                fromAddress = shopUiState.value.wallet!!,
                listingId = listingId,
                price = price
            )
        } else {
            shopUiState.value = shopUiState.value.copy(
                dialogContent = DialogContent(
                    title = "Not Authenticated",
                    text = "Please connect your wallet to continue"
                )
            )
        }
    }

    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return web3Repo.getCoinbaseSdk(openIntent)
    }

    private fun getTotalSold(listing: Int, callback: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalSold = SoldHelper.getTotalSold(listing.toLong()).toInt()
                callback.invoke(totalSold)
            } catch (_: Exception) {
                callback.invoke(0)
            }
        }
    }

    private fun mint(
        client: CoinbaseWalletSDK,
        fromAddress: String,
        listingId: String,
        price: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Web3Helper.mint(
                client,
                fromAddress = fromAddress,
                listingId = listingId,
                price = price,
                onMintFailure = { dialogContent ->
                    shopUiState.value = shopUiState.value.copy(dialogContent = dialogContent)
                }
            )
        }
    }
}