package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.daos.NftDetailsDao
import com.mashiverse.mashit.data.local.db.entities.NftDetailsEntity
import com.mashiverse.mashit.data.models.mashi.MashupDetails
import com.mashiverse.mashit.data.models.mashi.mappers.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    val alchemyRepo: AlchemyRepo,
    val nftDetailsDao: NftDetailsDao,
    val mashitRepo: MashitRepo,
) {
    val collectionFlow: Flow<List<NftDetailsEntity>> = nftDetailsDao.getNftDetails()

    suspend fun updateData(wallet: String) {
        val newCollectionList = alchemyRepo.getCollection(wallet)
        val oldCollectionList = nftDetailsDao.getNftDetails().first()

        val newNames = newCollectionList.map { it.name }.toSet()
        val oldNames = oldCollectionList.map { it.name }.toSet()

        val toAdd = newCollectionList.filter { it.name !in oldNames }
        val toDelete = oldCollectionList.filter { it.name !in newNames }

        if (toAdd.isNotEmpty()) {
            nftDetailsDao.insertNftDetails(toAdd.toEntities())
        }
        if (toDelete.isNotEmpty()) {
            nftDetailsDao.deleteNftDetails(toDelete)
        }
    }

    suspend fun getMashup(wallet: String): MashupDetails {
        return mashitRepo.getMashup(wallet)
    }
}