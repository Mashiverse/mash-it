package com.mashiverse.mashit.data.models.nft.mappers

import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import com.mashiverse.mashit.utils.helpers.nft.fromIpfsScheme

fun List<AlchemyDto.OwnedNft.Raw.Metadata.Asset>.toTraits() = this.map { asset ->
    Trait(
        url = asset.uri.fromIpfsScheme(),
        type = TraitType.valueOf(asset.label.uppercase())
    )
}