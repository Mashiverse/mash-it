package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.entities.NftEntity
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.mappers.toEntities
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
        val toDelete = oldCollection.filter { it.name !in newNames }

        if (toAdd.isNotEmpty()) {
            val list = toAdd.toEntities().map { it.copy(isOwned = true) }
            nftRepo.insertNfts(list)
        }

        if (toDelete.isNotEmpty()) {
            val list = toDelete.map { it.copy(isOwned = false) }
            nftRepo.updateNfts(list)
        }
    }

    suspend fun clearOwned() = nftRepo.clearOwned()

    suspend fun getMashup(wallet: String): MashupDetails {
        return mashitRepo.getMashup(wallet)
    }
}