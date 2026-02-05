package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.local.db.entities.MashiDetailsEntity
import com.mashiverse.mashit.data.models.mashi.NftDetails

fun NftDetails.MashiDetails.toEntity() = MashiDetailsEntity(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    mashiTraits = this.mashiTraits,
    productInfo = this.productInfo
)

fun MashiDetailsEntity.fromEntity() = NftDetails.MashiDetails(
    name = this.name,
    author = this.author,
    description = this.description,
    compositeUrl = this.compositeUrl,
    mashiTraits = this.mashiTraits,
    productInfo = this.productInfo
)

fun List<NftDetails.MashiDetails>.toEntities() = this.map {
    it.toEntity()
}

fun List<MashiDetailsEntity>.fromEntities() = this.map {
    it.fromEntity()
}