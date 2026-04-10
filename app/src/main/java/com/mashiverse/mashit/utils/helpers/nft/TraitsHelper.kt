package com.mashiverse.mashit.utils.helpers.nft

import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.activeTraits

fun getTraitsByType(nfts: List<Nft>): Map<TraitType, List<MashupTrait>> {
    val allMashupTraits = nfts.flatMap { nft ->
        nft.traits?.map { trait ->
            MashupTrait(
                trait = trait,
                avatarName = nft.name,
                mint = if (trait.type == TraitType.BACKGROUND) {
                    nft.owned?.getOrNull(0)?.mint
                } else {
                    null
                }
            )
        } ?: emptyList()
    }

    return allMashupTraits.groupBy { it.trait.type }
}

fun getRandomTraits(nfts: List<Nft>): List<MashupTrait> {
    val randomAssets = TraitType.entries.map { type ->
        val available = getTraitsByType(nfts)[type]

        if (type in activeTraits) {
            available?.randomOrNull() ?: MashupTrait(Trait(type, null), "")
        } else {
            if ((0..1).random() == 1) available?.randomOrNull() ?: MashupTrait(
                Trait(
                    type,
                    null
                ), ""
            )
            else MashupTrait(Trait(type, null), "")
        }
    }

    return randomAssets
}