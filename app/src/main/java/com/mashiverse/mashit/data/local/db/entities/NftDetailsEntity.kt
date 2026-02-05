package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.data.models.mashi.Trait

@Entity(tableName = "nft_details")
data class NftDetailsEntity(
    @PrimaryKey
    val name: String,
    val author: String,
    val description: String?,
    val compositeUrl: String,
    val traits: List<Trait>? = null,
    @Embedded(prefix = "product_")
    val productInfo: ProductInfo? = null,
)
