package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.data.models.mashi.ProductInfo

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
