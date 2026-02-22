package com.mashiverse.mashit.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashiverse.mashit.data.models.nft.Owned
import com.mashiverse.mashit.data.models.nft.ProductInfo
import com.mashiverse.mashit.data.models.nft.Trait

@Entity(tableName = "nfts")
data class NftEntity(
    @PrimaryKey
    val name: String,
    val author: String,
    val description: String?,
    val compositeUrl: String,
    val traits: List<Trait>? = null,
    @Embedded(prefix = "product_")
    val productInfo: ProductInfo? = null,
    val owned: List<Owned>? = null,
    val isOwned: Boolean = false
)
