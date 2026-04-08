package com.mashiverse.mashit.ui.screens.artists.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.artists.ArtistInfo
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.repos.ArtistsRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.states.intents.ImageIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ArtistsPreviewViewModel @Inject constructor(
    private val imageTypeRepo: ImageTypeRepo,
    artistsRepo: ArtistsRepo
) : ViewModel() {
    val artistsPagingData: Flow<PagingData<ArtistInfo>> = artistsRepo.getArtistsPagingData()
        .cachedIn(viewModelScope)

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.GetImageType -> getImageType(intent.url, intent.onResult)

            is ImageIntent.SetImageType -> setImageType(intent.url, intent.imageType)
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
}