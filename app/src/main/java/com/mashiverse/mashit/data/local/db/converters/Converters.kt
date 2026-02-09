package com.mashiverse.mashit.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.PriceCurrency
import com.mashiverse.mashit.data.models.mashi.Trait

class NftConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromTraits(value: List<Trait>?): String {
        return gson.toJson(value ?: emptyList<Trait>())
    }

    @TypeConverter
    fun toTraits(value: String): List<Trait> {
        val listType = object : TypeToken<List<Trait>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromOwned(value: List<Owned>?): String {
        return gson.toJson(value ?: emptyList<Owned>())
    }

    @TypeConverter
    fun toOwned(value: String): List<Owned> {
        val listType = object : TypeToken<List<Owned>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromPriceCurrency(value: PriceCurrency): String = value.name

    @TypeConverter
    fun toPriceCurrency(value: String): PriceCurrency = PriceCurrency.valueOf(value)
}

class ImageTypeConverters {
    @TypeConverter
    fun fromImageType(value: ImageType): String = value.name

    @TypeConverter
    fun toImageType(value: String): ImageType = ImageType.valueOf(value)
}