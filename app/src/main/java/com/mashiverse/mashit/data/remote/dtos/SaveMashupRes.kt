package com.mashiverse.mashit.data.remote.dtos

import com.google.gson.annotations.SerializedName
import com.mashiverse.mashit.data.models.mashup.save.MashupData
import kotlinx.serialization.Serializable

@Serializable
data class SaveMashupRes(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MashupData
)