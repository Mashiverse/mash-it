package com.mashiverse.mashit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashiverse.mashit.data.repos.DatastoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val datastoreRepo: DatastoreRepo
) : ViewModel() {

    init {

    }

    fun observeThemeState() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}