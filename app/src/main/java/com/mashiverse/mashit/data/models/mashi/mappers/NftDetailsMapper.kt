package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.local.db.entities.NftEntity
import com.mashiverse.mashit.data.models.mashi.Nft

fun Nft.toEntity() = NftEntity(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    productInfo = this.productInfo
)

fun NftEntity.fromEntity() = Nft(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    productInfo = this.productInfo
)

fun List<Nft>.toEntities() = this.map {
    it.toEntity()
}

fun List<NftEntity>.fromEntities() = this.map {
    it.fromEntity()
}