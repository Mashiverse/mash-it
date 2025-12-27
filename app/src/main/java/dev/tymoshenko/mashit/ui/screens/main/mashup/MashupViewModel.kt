package dev.tymoshenko.mashit.ui.screens.main.mashup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.models.color.ColorType
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(): ViewModel() {
    private val _body = mutableStateOf(Color(38, 24, 4))
    val body: State<Color> get() = _body

    private val _eyes = mutableStateOf(Color(38, 24, 4))
    val eyes: State<Color> get() = _eyes

    private val _hair = mutableStateOf(Color(38, 24, 4))
    val hair: State<Color> get() = _hair

    private val _selectedColorType = mutableStateOf(ColorType.BODY)
    val selectedColorType: State<ColorType> get() = _selectedColorType

    fun selectColorType(colorType: ColorType) {
        _selectedColorType.value = colorType
    }

    fun changeBody(color: Color) {
        _body.value = color
    }

    fun changeEyes(color: Color) {
        _eyes.value = color
    }

    fun changeHair(color: Color) {
        _hair.value = color
    }
}