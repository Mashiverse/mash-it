package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.models.mashi.MashiDetails
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.remote.apis.AlchemyApi
import com.mashiverse.mashit.utils.MASHI_CONTRACT
import javax.inject.Inject

class AlchemyRepo @Inject constructor(
    private val alchemyApi: AlchemyApi
) {
    suspend fun getCollection(wallet: String): List<MashiDetails> {
        val nfts: MutableList<MashiDetails> = mutableListOf()
        var key: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    apiKey = BuildConfig.ALCHEMY_API_KEY,
                    withMetadata = true,
                    owner = "0x60873c2d9d575514df19cbae29040c6c9915377a",
                    contractAddress = MASHI_CONTRACT,
                    pageKey = key
                )

                val ownedNfts = data.ownedNfts
                if (ownedNfts.isEmpty()) return emptyList()

                for (i in 0 until ownedNfts.size) {
                    val nft = ownedNfts[i]
                    val metadata = nft.raw.metadata

                    val name = metadata.name
                    val (mashiName, authorName) = if (" by " in name) {
                        val parts = name.split(" by ", limit = 2)
                        parts[0] to parts[1]
                    } else {
                        name to ""
                    }
                    val description = metadata.description
                    val compositeUrl = metadata.image.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")

                    val assets = metadata.assets

                    val mashiTraits = assets.map { asset ->
                        MashiTrait(
                            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/"),
                            traitType = TraitType.valueOf(asset.label.uppercase())
                        )
                    }

                    nfts.add(
                        MashiDetails(
                            name = mashiName,
                            description = description,
                            compositeUrl = compositeUrl,
                            mashiTraits = mashiTraits,
                            author = authorName
                        )
                    )
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