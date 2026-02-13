package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.entities.NftEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NftRepo @Inject constructor(
    val nftDao: NftDao,
) {
    val ownedNftsFlow: Flow<List<NftEntity>> = nftDao.getOwnedNfts()

    suspend fun deleteNfts(nfts: List<NftEntity>) = nftDao.deleteNfts(nfts)

    suspend fun insertNft(nft: NftEntity) {
        val currentNft = nftDao.getNftByName(nft.name)

        if (currentNft == null) {
            nftDao.insertNft(nft)
        } else {
            var tempNft = currentNft!!

            nft.productInfo?.let {
                tempNft = tempNft.copy(productInfo = nft.productInfo)
            }

            nft.traits?.let {
                tempNft = tempNft.copy(traits = nft.traits)
            }

            nft.owned?.let {
                tempNft = tempNft.copy(owned = nft.owned)
            }

            if (nft.isOwned) {
                tempNft = tempNft.copy(isOwned = true)
            }

            nftDao.insertNft(tempNft)
        }
    }

    suspend fun clearOwned() {
        val ownedNfts = ownedNftsFlow.first()
        nftDao.deleteNfts(ownedNfts)
    }

    suspend fun insertNfts(nfts: List<NftEntity>) {
        nfts.forEach { nft ->
            insertNft(nft)
        }
    }
}