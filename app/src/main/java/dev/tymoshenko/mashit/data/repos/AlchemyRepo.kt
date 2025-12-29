package dev.tymoshenko.mashit.data.repos

import android.util.Log
import dev.tymoshenko.mashit.BuildConfig
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.data.remote.apis.AlchemyApi
import dev.tymoshenko.mashit.utils.MASHI_CONTRACT
import javax.inject.Inject

class AlchemyRepo @Inject constructor(
    private val alchemyApi: AlchemyApi
) {
    suspend fun getCollection(wallet: String = ""): List<MashiDetails> {
        val nfts: MutableList<MashiDetails> = mutableListOf()
        var key: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    apiKey = BuildConfig.ALCHEMY_API_KEY,
                    withMetadata = true,
                    owner = "0xd3145ee544a1d8fc5802b5861e2777a2be2ebb5a",
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

                    val traits = assets.map { asset ->
                        Trait(
                            url = asset.uri.replace("ipfs://", "https://ipfs.filebase.io/ipfs/"),
                            traitType = TraitType.valueOf(asset.label.uppercase())
                        )
                    }

                    nfts.add(
                        MashiDetails(
                            name = mashiName,
                            description = description,
                            compositeUrl = compositeUrl,
                            traits = traits,
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