package io.mashit.mashit.ui.screens.main.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.repos.CollectionRepo
import io.mashit.mashit.data.repos.DataStoreRepo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DataStoreRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletPreferencesFlow
    val collectionFlow = collectionRepo.getCollectionFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedMashi = mutableStateOf<MashiDetails?>(null)
    val selectedMashi: State<MashiDetails?> get() = _selectedMashi

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

    fun selectMashi(mashi: MashiDetails) {
        _selectedMashi.value = mashi
    }
}