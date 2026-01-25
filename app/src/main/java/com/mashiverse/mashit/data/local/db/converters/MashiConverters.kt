package com.mashiverse.mashit.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.TraitType

class MashiConverters {
    private val gson = Gson()

    // Converts List<MashiTrait> to JSON String and back
    @TypeConverter
    fun fromMashiTraitList(value: List<MashiTrait>?): String {
        return gson.toJson(value ?: emptyList<MashiTrait>())
    }

    @TypeConverter
    fun toMashiTraitList(value: String): List<MashiTrait> {
        val listType = object : TypeToken<List<MashiTrait>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    // Converts the TraitType Enum to String and back
    @TypeConverter
    fun fromTraitType(value: TraitType): String = value.name

    @TypeConverter
    fun toTraitType(value: String): TraitType = TraitType.valueOf(value)

    // Converts the PriceCurrency Enum to String and back
    @TypeConverter
    fun fromPriceCurrency(value: PriceCurrency): String = value.name

    @TypeConverter
    fun toPriceCurrency(value: String): PriceCurrency = PriceCurrency.valueOf(value)
}