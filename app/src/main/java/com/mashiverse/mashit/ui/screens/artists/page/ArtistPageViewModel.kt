package com.mashiverse.mashit.ui.screens.artists.page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.artists.ArtistPageInfo
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.repos.ArtistsRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.utils.helpers.web3.SoldHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistPageViewModel @Inject constructor(
    private val imageTypeRepo: ImageTypeRepo,
    private val artistsRepo: ArtistsRepo,
) : ViewModel() {

    private val _pageInfo = mutableStateOf<ArtistPageInfo?>(null)
    val pageInfo: State<ArtistPageInfo?> get() = _pageInfo

    fun getListingsPagingData(alias: String): Flow<PagingData<Nft>> =
        artistsRepo.getListingsPagingData(alias)


    fun fetchArtistPage(alias: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _pageInfo.value = artistsRepo.getArtistsPage(alias)
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
}