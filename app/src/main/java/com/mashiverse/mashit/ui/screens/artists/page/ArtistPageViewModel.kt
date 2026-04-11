package com.mashiverse.mashit.ui.screens.artists.page

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.DialogIntent
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.repos.ArtistsRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashitRepo
import com.mashiverse.mashit.data.repos.Web3Repo
import com.mashiverse.mashit.data.states.ArtistPageUiState
import com.mashiverse.mashit.utils.helpers.web3.SoldHelper
import com.mashiverse.mashit.utils.helpers.web3.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArtistPageViewModel @Inject constructor(
    private val imageTypeRepo: ImageTypeRepo,
    private val artistsRepo: ArtistsRepo,
    dataStoreRepo: DatastoreRepo,
    private val web3Repo: Web3Repo,
    private val mashitRepo: MashitRepo
) : ViewModel() {

    var artistPageUiState = mutableStateOf(ArtistPageUiState())
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
                    artistPageUiState.value = artistPageUiState.value.copy(wallet = wallet)
                } else {
                    artistPageUiState.value = artistPageUiState.value.copy(wallet = null)
                }
            }
        }
    }


    fun onInit(alias: String) {
        fetchItems(alias)
        fetchArtistPage(alias)
    }

    private fun fetchItems(alias: String) {
        artistPageUiState.value = artistPageUiState.value.copy(
            itemsData = artistsRepo.getListingsPagingData(alias)
                .cachedIn(viewModelScope)
        )
    }

    private fun fetchArtistPage(alias: String) {
        viewModelScope.launch(Dispatchers.IO) {
            artistPageUiState.value = artistPageUiState.value.copy(
                pageInfo = artistsRepo.getArtistsPage(alias)
            )
        }
    }

    // Shop Intent

    fun processShopIntent(intent: ShopIntent) {
        when (intent) {
            is ShopIntent.OnNftSelect -> selectNft(intent.id)

            is ShopIntent.OnNftDeselect -> deselectNft()

            else -> {}
        }
    }

    private fun selectNft(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                artistPageUiState.value = artistPageUiState.value.copy(
                    selectedNft = mashitRepo.getShopItem(id),
                    isExpanded = true
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch shop item details")
            }
        }
    }

    private fun deselectNft() {
        artistPageUiState.value = artistPageUiState.value.copy(
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

    private fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    private fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = ImageTypeEntity(url, imageType)
            imageTypeRepo.insertImageType(entity)
        }
    }

    // Dialog

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.OnClear -> artistPageUiState.value =
                artistPageUiState.value.copy(dialogContent = null)

            is DialogIntent.OnChange -> artistPageUiState.value =
                artistPageUiState.value.copy(dialogContent = intent.content)
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
            artistPageUiState.value = artistPageUiState.value.copy(
                dialogContent = DialogContent(
                    title = "POL Currency",
                    text = "We currently don't support POL minting"
                )
            )
        } else if (artistPageUiState.value.wallet != null) {
            mint(
                client = clientRef,
                fromAddress = artistPageUiState.value.wallet!!,
                listingId = listingId,
                price = price
            )
        } else {
            artistPageUiState.value = artistPageUiState.value.copy(
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
                    artistPageUiState.value =
                        artistPageUiState.value.copy(dialogContent = dialogContent)
                }
            )
        }
    }
}