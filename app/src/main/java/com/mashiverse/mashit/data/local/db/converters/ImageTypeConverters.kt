package com.mashiverse.mashit.data.local.db.converters

import androidx.room.TypeConverter
import com.mashiverse.mashit.data.models.sys.image.ImageType

class ImageTypeConverters {
    @TypeConverter
    fun fromImageType(value: ImageType): String = value.name

    @TypeConverter
    fun toImageType(value: String): ImageType = ImageType.valueOf(value)
}