package com.mashiverse.mashit.ui.screens.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mashiverse.mashit.data.local.db.entities.TraitTypeEntity
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DataStoreRepo
import com.mashiverse.mashit.data.repos.TraitTypeRepo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CollectionViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DataStoreRepo,
    private val traitTypeRepo: TraitTypeRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.getCollectionFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedMashi = mutableStateOf<NftDetails.MashiDetails?>(null)
    val selectedMashi: State<NftDetails.MashiDetails?> get() = _selectedMashi

    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletPreferences
                .distinctUntilChanged()
                .collect { prefs ->
                if (prefs.wallet != null) {
                    collectionRepo.updateData(prefs.wallet)
                }
            }
        }
    }

    fun selectMashi(mashi: NftDetails.MashiDetails) {
        _selectedMashi.value = mashi
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