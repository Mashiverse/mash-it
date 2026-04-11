package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Owned
import com.mashiverse.mashit.data.models.nft.mappers.toTraits
import com.mashiverse.mashit.data.remote.apis.AlchemyApi
import com.mashiverse.mashit.utils.ALCHEMY_KEY
import com.mashiverse.mashit.utils.MASHI_ADDRESS
import com.mashiverse.mashit.utils.helpers.nft.parseName
import com.mashiverse.mashit.utils.helpers.nft.fromIpfsScheme
import javax.inject.Inject

class AlchemyRepo @Inject constructor(
    private val alchemyApi: AlchemyApi
) {

    suspend fun getCollection(wallet: String): List<Nft> {
        val nfts: MutableList<Nft> = mutableListOf()
        var key: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    apiKey = ALCHEMY_KEY,
                    withMetadata = true,
                    owner = wallet,
                    contractAddress = MASHI_ADDRESS,
                    pageKey = key
                )

                val ownedNfts = data.ownedNfts
                if (ownedNfts.isEmpty()) return emptyList()

                ownedNfts.forEach { nft ->
                    val metadata = nft.raw.metadata
                    val details = parseName(metadata.name)
                    val description = metadata.description

                    val assets = metadata.assets
                    val compositeUrl = metadata.image.fromIpfsScheme()
                    val traits = assets.toTraits()

                    val currentOwned = Owned(
                        mint = details.mint,
                        timestamp = nft.timeLastUpdated
                    )

                    val tempNft = nfts.firstOrNull { nft -> nft.name == details.name }

                    tempNft?.let {
                        val owned = tempNft.owned?.toMutableList() ?: mutableListOf()
                        owned.add(currentOwned)

                        val updatedNft = tempNft.copy(owned = owned)

                        nfts.remove(tempNft)
                        nfts.add(updatedNft)

                        return@forEach
                    }

                    nfts.add(
                        Nft(
                            name = details.name,
                            description = description,
                            compositeUrl = compositeUrl,
                            traits = traits,
                            author = details.authorName,
                            owned = listOf(currentOwned)
                        )
                    )
                }

                key = data.pageKey
                if (key == null) return nfts
            }
        } catch (_: Exception) {
            return emptyList()
        }
    }
}