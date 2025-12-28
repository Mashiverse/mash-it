package dev.tymoshenko.mashit.ui.screens.main.collection

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.models.mashi.MashiCollectionItem
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.multipleExample
import dev.tymoshenko.mashit.data.repos.AlchemyRepo
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@HiltViewModel
class CollectionViewModel @Inject constructor(alchemyRepo: AlchemyRepo): ViewModel() {
    private val _mashies = mutableStateOf<List<MashiDetails>>(listOf())
    val mashies: State<List<MashiDetails>> get() = _mashies

    private val _selectedMashi = mutableStateOf<MashiDetails?>(null)
    val selectedMashi: State<MashiDetails?> get() = _selectedMashi

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val collection = alchemyRepo.getCollection()
            _mashies.value = collection.map { item ->
                MashiDetails(
                    name = item.name,
                    author = item.author,
                    description = item.description,
                    perWallet = 0,
                    soldQuantity = 0,
                    quantity = 0,
                    compositeUrl = item.compositeUrl,
                    traits = item.traits,
                    price = 0
                )
            }
        }
    }

    fun selectMashi(mashi: MashiDetails) {
        _selectedMashi.value = mashi
    }
}