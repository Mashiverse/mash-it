package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import jakarta.inject.Inject


@HiltViewModel
class ShopViewModel @Inject constructor(): ViewModel() {
    private val _mashies = mutableStateOf<List<MashiDetails>>(listOf())
    val mashies: State<List<MashiDetails>> get() = _mashies

    private val _selectedMashi = mutableStateOf<MashiDetails?>(null)
    val selectedMashi: State<MashiDetails?> get() = _selectedMashi

    init {
        _mashies.value = emptyList()
    }

    fun selectMashi(mashi: MashiDetails) {
        _selectedMashi.value = mashi
    }
}