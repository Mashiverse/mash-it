package io.mashit.mashit.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.mashit.mashit.data.models.image.ImageType

@Entity(tableName = "trait_types")
data class TraitTypeEntity(
    @PrimaryKey
    val url: String,
    val type: ImageType
)
