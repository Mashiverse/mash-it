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
    private val _body = mutableStateOf(Color.Green)
    val body: State<Color> get() = _body

    private val _eyes = mutableStateOf(Color.Yellow)
    val eyes: State<Color> get() = _eyes

    private val _hair = mutableStateOf(Color.Blue)
    val hair: State<Color> get() = _hair

    private val _selectedColorType = mutableStateOf(ColorType.BODY)
    val selectedColorType: State<ColorType> get() = _selectedColorType

    private val _mashies = mutableStateOf<List<MashiDetails>>(listOf())
    val mashies: State<List<MashiDetails>> get() = _mashies

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

    fun changeMashupTrait(
        trait: Trait
    ) {
        Log.d("Mashup", "here")
        when (trait.traitType) {
            TraitType.BACKGROUND -> if (_mashupDetails.value.background?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(background = null)
            } else {
                _mashupDetails.value = mashupDetails.value.copy(
                    background = Trait(
                        traitType = TraitType.BACKGROUND,
                        url = trait.url
                    )
                )
            }

            TraitType.HAIR_BACK -> if (_mashupDetails.value.hairBack?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(hairBack = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(hairBack = Trait(
                        traitType = TraitType.HAIR_BACK,
                        url = trait.url
                    )
                    )
            }

            TraitType.CAPE -> if (_mashupDetails.value.cape?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(cape = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(cape = Trait(
                        traitType = TraitType.CAPE,
                        url = trait.url
                    ))
            }

            TraitType.BOTTOM -> if (_mashupDetails.value.bottom?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(bottom = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(bottom = Trait(
                        traitType = TraitType.BOTTOM,
                        url = trait.url
                    ))
            }

            TraitType.UPPER -> if (_mashupDetails.value.upper?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(upper = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(upper = Trait(
                        traitType = TraitType.UPPER,
                        url = trait.url
                    ))
            }

            TraitType.HEAD -> if (_mashupDetails.value.head?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(head = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(head = Trait(
                        traitType = TraitType.HEAD,
                        url = trait.url
                    ))
            }

            TraitType.EYES -> if (_mashupDetails.value.eyes?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(eyes = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(eyes =Trait(
                        traitType = TraitType.EYES,
                        url = trait.url
                    ))
            }

            TraitType.HAIR_FRONT -> if (_mashupDetails.value.hairFront?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(hairFront = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(hairFront = Trait(
                        traitType = TraitType.HAIR_FRONT,
                        url = trait.url
                    ))
            }

            TraitType.HAT -> if (_mashupDetails.value.hat?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(hat = null)
            } else {
                _mashupDetails.value =
                    mashupDetails.value.copy(hat = Trait(
                        traitType = TraitType.HAT,
                        url = trait.url
                    ))
            }

            TraitType.LEFT_ACCESSORY -> if (_mashupDetails.value.leftAccessory?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(leftAccessory = null)
            } else {
                _mashupDetails.value = mashupDetails.value.copy(
                    leftAccessory = Trait(
                        traitType = TraitType.LEFT_ACCESSORY,
                        url = trait.url
                    )
                )
            }

            TraitType.RIGHT_ACCESSORY -> if (_mashupDetails.value.rightAccessory?.url == trait.url) {
                _mashupDetails.value = _mashupDetails.value.copy(rightAccessory = null)
            } else {
                _mashupDetails.value = mashupDetails.value.copy(
                    rightAccessory = Trait(
                        traitType = TraitType.RIGHT_ACCESSORY,
                        url = trait.url
                    )
                )
            }
        }
    }

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