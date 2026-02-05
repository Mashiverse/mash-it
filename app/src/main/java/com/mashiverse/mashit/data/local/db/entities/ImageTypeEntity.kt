package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashiverse.mashit.data.models.image.ImageType

@Entity(tableName = "trait_types")
data class ImageTypeEntity(
    @PrimaryKey
    val url: String,
    val type: ImageType
)
