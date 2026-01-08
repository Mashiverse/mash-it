package io.mashit.mashit.ui.screens.main.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.repos.AlchemyRepo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class CollectionViewModel @Inject constructor(alchemyRepo: AlchemyRepo): ViewModel() {
    private val _mashies = mutableStateOf<List<MashiDetails>>(listOf())
    val mashies: State<List<MashiDetails>> get() = _mashies

    private val _selectedMashi = mutableStateOf<MashiDetails?>(null)
    val selectedMashi: State<MashiDetails?> get() = _selectedMashi

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _mashies.value = alchemyRepo.getCollection().sortedBy { mashie ->
                mashie.name
            }
        }
    }

    fun selectMashi(mashi: MashiDetails) {
        _selectedMashi.value = mashi
    }
}