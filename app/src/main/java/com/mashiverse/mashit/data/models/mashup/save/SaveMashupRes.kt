import com.mashiverse.mashit.data.models.mashup.save.MashupData
import kotlinx.serialization.Serializable

@Serializable
data class SaveMashupRes(
    val success: Boolean,
    val message: String,
    val data: MashupData // Reuses the data class from the request
)