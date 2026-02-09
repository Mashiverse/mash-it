package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.remote.apis.AlchemyApi
import com.mashiverse.mashit.utils.ALCHEMY_KEY
import com.mashiverse.mashit.utils.MASHI_ADDRESS
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

                for (i in 0 until ownedNfts.size) {
                    val nft = ownedNfts[i]

                    val metadata = nft.raw.metadata

                    val name = metadata.name
                    val (nftMintedName: String, authorName: String) = if (" by " in name) {
                        val parts = name.split(" by ", limit = 2)
                        parts[0] to parts[1]
                    } else {
                        "" to ""
                    }

                    val (nftName: String, mint: Int) = if (" #" in nftMintedName) {
                        val parts = nftMintedName.split(" #", limit = 2)
                        parts[0] to parts[1].toInt()
                    } else {
                        "" to -1
                    }


                    val description = metadata.description
                    val compositeUrl =
                        metadata.image.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")

                    val assets = metadata.assets

                    val traits = assets.map { asset ->
                        Trait(
                            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/"),
                            type = TraitType.valueOf(asset.label.uppercase())
                        )
                    }

                    val currentOwned = Owned(
                        mint = mint,
                        timestamp = nft.timeLastUpdated
                    )

                    val tempNft = nfts.firstOrNull { nft -> nft.name == nftName }

                    if (tempNft == null) {
                        nfts.add(
                            Nft(
                                name = nftName,
                                description = description,
                                compositeUrl = compositeUrl,
                                traits = traits,
                                author = authorName,
                                owned = listOf(currentOwned)
                            )
                        )
                    } else {
                        val owned = tempNft.owned?.toMutableList() ?: mutableListOf()
                        owned.add(currentOwned)

                        val updatedNft = tempNft.copy(owned = owned)

                        nfts.remove(tempNft)
                        nfts.add(updatedNft)
                    }
                }

                key = data.pageKey
                if (key == null) return nfts
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}