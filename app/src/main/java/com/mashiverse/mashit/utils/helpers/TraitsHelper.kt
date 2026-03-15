package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.TraitType

object TraitsHelper {
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
}