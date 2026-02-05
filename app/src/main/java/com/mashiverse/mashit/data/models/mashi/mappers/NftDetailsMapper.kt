package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.local.db.entities.NftDetailsEntity
import com.mashiverse.mashit.data.models.mashi.NftDetails

fun NftDetails.toEntity() = NftDetailsEntity(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    productInfo = this.productInfo
)

fun NftDetailsEntity.fromEntity() = NftDetails(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    productInfo = this.productInfo
)

fun List<NftDetails>.toEntities() = this.map {
    it.toEntity()
}

fun List<NftDetailsEntity>.fromEntities() = this.map {
    it.fromEntity()
}