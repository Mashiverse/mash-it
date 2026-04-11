package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.entities.NftEntity
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.nft.mappers.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    val alchemyRepo: AlchemyRepo,
    val nftRepo: NftRepo,
    val mashitRepo: MashitRepo,
) {
    val collectionFlow: Flow<List<NftEntity>> = nftRepo.ownedNftsFlow

    suspend fun updateOwnedData(wallet: String) {
        val newCollection = alchemyRepo.getCollection(wallet)
        val oldCollection = nftRepo.ownedNftsFlow.first()

        val newNames = newCollection.map { it.name }.toSet()
        val oldNames = oldCollection.map { it.name }.toSet()

        val toAdd = newCollection.filter { it.name !in oldNames }
        val toRemove = oldCollection.filter { it.name !in newNames }
        val toUpdate = newCollection.mapNotNull { new ->
            val old = oldCollection.find { it.name == new.name }

            if (old != null && new.owned != old.owned) {
                new
            } else null
        }

        if (toUpdate.isNotEmpty()) {
            val list = toUpdate.toEntities()
            nftRepo.insertNfts(list)
        }

        if (toAdd.isNotEmpty()) {
            val list = toAdd.toEntities()
            nftRepo.insertNfts(list)
        }

        if (toRemove.isNotEmpty()) {
            nftRepo.deleteNfts(toRemove)
        }
    }

    suspend fun clearOwned() = nftRepo.clearOwned()

    suspend fun getMashup(wallet: String): MashupDetails {
        return mashitRepo.getMashup(wallet)
    }
}