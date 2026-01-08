package io.mashit.mashit.ui.screens.main.mashup

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mashit.mashit.data.models.color.ColorType
import io.mashit.mashit.data.models.mashi.MashiDetails
import io.mashit.mashit.data.models.mashi.MashupDetails
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.TraitType
import io.mashit.mashit.data.repos.AlchemyRepo
import io.mashit.mashit.data.repos.MashiRepo
import io.mashit.mashit.utils.io.saveImageToGallery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    alchemyRepo: AlchemyRepo,
    private val mashiRepo: MashiRepo
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
            _mashies.value  = alchemyRepo.getCollection().sortedBy { mashie ->
                mashie.name
            }
        }
    }

    fun changeMashupTrait(mashiTrait: MashiTrait) {
        val toggleMashiTrait: (MashiTrait?, MashiTrait?) -> MashiTrait? = { currentMashiTrait: MashiTrait?, newMashiTrait: MashiTrait? ->
            if (currentMashiTrait?.url == newMashiTrait?.url) null else newMashiTrait
        }

        val current = _mashupDetails.value
        val updated = when (mashiTrait.traitType) {
            TraitType.BACKGROUND -> current.copy(
                background = toggleMashiTrait(
                    current.background,
                    mashiTrait
                )
            )

            TraitType.HAIR_BACK -> current.copy(hairBack = toggleMashiTrait(current.hairBack, mashiTrait))
            TraitType.CAPE -> current.copy(cape = toggleMashiTrait(current.cape, mashiTrait))
            TraitType.BOTTOM -> current.copy(bottom = toggleMashiTrait(current.bottom, mashiTrait))
            TraitType.UPPER -> current.copy(upper = toggleMashiTrait(current.upper, mashiTrait))
            TraitType.HEAD -> current.copy(head = toggleMashiTrait(current.head, mashiTrait))
            TraitType.EYES -> current.copy(eyes = toggleMashiTrait(current.eyes, mashiTrait))
            TraitType.HAIR_FRONT -> current.copy(hairFront = toggleMashiTrait(current.hairFront, mashiTrait))
            TraitType.HAT -> current.copy(hat = toggleMashiTrait(current.hat, mashiTrait))
            TraitType.LEFT_ACCESSORY -> current.copy(
                leftAccessory = toggleMashiTrait(
                    current.leftAccessory,
                    mashiTrait
                )
            )

            TraitType.RIGHT_ACCESSORY -> current.copy(
                rightAccessory = toggleMashiTrait(
                    current.rightAccessory,
                    mashiTrait
                )
            )
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

    fun saveMashup(ctx: Context, wallet: String = "0xae42edc0fc636d1214a6c5d829c37d778398f17b", isStatic: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val mashupResult = mashiRepo.getMashup(wallet, isStatic)
            val timestamp = System.currentTimeMillis()
            val fileName = if (mashupResult.contentType == "image/png") {
                "mashup_$timestamp.png"
            } else {
                "mashup_$timestamp.gif"
            }

            saveImageToGallery(
                context = ctx,
                imageBytes = mashupResult.bytes,
                fileName = fileName,
                mimeType = mashupResult.contentType
            )

            withContext(Dispatchers.Main) {
                Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun randomize(mashupDetails: MashupDetails) {
        _mashupDetails.value = mashupDetails
    }
}