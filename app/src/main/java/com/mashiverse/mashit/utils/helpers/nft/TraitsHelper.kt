package com.mashiverse.mashit.utils.helpers.nft

import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.activeTraits

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

fun sortNfts(sortType: SortType, nfts: List<Nft>): List<Nft> {
    return when (sortType) {
        SortType.NEWEST -> nfts.sortedByDescending { it.owned?.get(0)?.timestamp }
        SortType.OLDEST -> nfts.sortedBy { it.owned?.get(0)?.timestamp }
        // Sort by Name (A-Z), then by Mint (Ascending)
        SortType.ALPHABET_ASC -> nfts
            .sortedWith(compareBy<Nft> { it.name.lowercase() }
                .thenBy { it.owned?.firstOrNull()?.mint })

        // Sort by Name (Z-A), then by Mint (Ascending)
        SortType.ALPHABET_DESC -> nfts
            .sortedWith(compareByDescending<Nft> { it.name.lowercase() }
                .thenBy { it.owned?.firstOrNull()?.mint })
    }
}

fun String.toIpfsPartialUri() = this.replace("https://ipfs.filebase.io/ipfs/", "")