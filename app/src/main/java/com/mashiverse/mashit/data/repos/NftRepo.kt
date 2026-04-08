package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.entities.NftEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class NftRepo @Inject constructor(
    val nftDao: NftDao,
) {
    val ownedNftsFlow: Flow<List<NftEntity>> = nftDao.getOwnedNfts()

    suspend fun insertNfts(nfts: List<NftEntity>) {
        nfts.forEach { nft ->
            insertNft(nft)
        }
    }

    suspend fun insertNft(nft: NftEntity) {
        val currentNft = nftDao.getNftByName(nft.name)

        if (currentNft == null) {
            nftDao.insertNft(nft)
            return
        }

        val updatedNft = currentNft.copy(
            productInfo = nft.productInfo ?: currentNft.productInfo,
            traits = nft.traits ?: currentNft.traits,
            owned = nft.owned ?: currentNft.owned,
            isOwned = true
        )

        nftDao.updateNft(updatedNft)
    }

    suspend fun clearOwned() {
        val ownedNfts = ownedNftsFlow.first()
        nftDao.deleteNfts(ownedNfts)
    }

    suspend fun deleteNfts(nfts: List<NftEntity>) = nftDao.deleteNfts(nfts)
}