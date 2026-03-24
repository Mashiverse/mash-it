package com.mashiverse.mashit.data.models.nft.mappers

import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import com.mashiverse.mashit.utils.helpers.toFilebaseHttp

fun List<AlchemyDto.OwnedNft.Raw.Metadata.Asset>.toTraits() = this.map { asset ->
    Trait(
        url = asset.uri.toFilebaseHttp(),
        type = TraitType.valueOf(asset.label.uppercase())
    )
}