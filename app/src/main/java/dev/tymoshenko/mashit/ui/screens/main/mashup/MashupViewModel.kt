package dev.tymoshenko.mashit.ui.screens.main.mashup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.MashupDetails
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.data.repos.AlchemyRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    alchemyRepo: AlchemyRepo
) : ViewModel() {
    // Colors
    private val _body = mutableStateOf(Color.Green)
    val body: State<Color> get() = _body
    private val _eyes = mutableStateOf(Color.Yellow)
    val eyes: State<Color> get() = _eyes
    private val _hair = mutableStateOf(Color.Blue)
    val hair: State<Color> get() = _hair
    private val _selectedColorType = mutableStateOf(ColorType.BODY)
    val selectedColorType: State<ColorType> get() = _selectedColorType

    // Mashies
    private val _mashies = mutableStateOf<List<MashiDetails>>(listOf())
    val mashies: State<List<MashiDetails>> get() = _mashies

    //Mashup
    private val _mashupDetails = mutableStateOf(MashupDetails())
    val mashupDetails: State<MashupDetails> get() = _mashupDetails

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

    fun changeMashupTrait(trait: Trait) {
        val toggleTrait: (Trait?, Trait?) -> Trait? = { currentTrait: Trait?, newTrait: Trait? ->
            if (currentTrait?.url == newTrait?.url) null else newTrait
        }

        val current = _mashupDetails.value
        val updated = when (trait.traitType) {
            TraitType.BACKGROUND -> current.copy(background = toggleTrait(current.background, trait))
            TraitType.HAIR_BACK -> current.copy(hairBack = toggleTrait(current.hairBack, trait))
            TraitType.CAPE -> current.copy(cape = toggleTrait(current.cape, trait))
            TraitType.BOTTOM -> current.copy(bottom = toggleTrait(current.bottom, trait))
            TraitType.UPPER -> current.copy(upper = toggleTrait(current.upper, trait))
            TraitType.HEAD -> current.copy(head = toggleTrait(current.head, trait))
            TraitType.EYES -> current.copy(eyes = toggleTrait(current.eyes, trait))
            TraitType.HAIR_FRONT -> current.copy(hairFront = toggleTrait(current.hairFront, trait))
            TraitType.HAT -> current.copy(hat = toggleTrait(current.hat, trait))
            TraitType.LEFT_ACCESSORY -> current.copy(leftAccessory = toggleTrait(current.leftAccessory, trait))
            TraitType.RIGHT_ACCESSORY -> current.copy(rightAccessory = toggleTrait(current.rightAccessory, trait))
        }

        _mashupDetails.value = updated
    }

    fun selectColorType(colorType: ColorType) {
        _selectedColorType.value = colorType
    }

    fun changeColor(color: Color, colorType: ColorType) {
        when (colorType) {
            ColorType.BODY -> _body.value = color
            ColorType.EYES -> _eyes.value = color
            ColorType.HAIR -> _hair.value = color
        }
    }
}