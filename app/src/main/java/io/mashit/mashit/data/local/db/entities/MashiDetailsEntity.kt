package io.mashit.mashit.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.ProductInfo

@Entity(tableName = "mashi_details")
data class MashiDetailsEntity(
    @PrimaryKey
    val name: String,
    val author: String,
    val description: String,
    val compositeUrl: String,
    val mashiTraits: List<MashiTrait>,
    @Embedded(prefix = "product_")
    val productInfo: ProductInfo? = null
)
