package io.mashit.mashit.data.repos

import io.mashit.mashit.data.local.db.daos.MashiDetailsDao
import io.mashit.mashit.data.local.db.entities.MashiDetailsEntity
import io.mashit.mashit.data.models.mashi.mappers.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CollectionRepo @Inject constructor(
    val alchemyRepo: AlchemyRepo,
    val mashiDetailsDao: MashiDetailsDao,
) {
    fun getCollectionFlow(): Flow<List<MashiDetailsEntity>> = mashiDetailsDao.getMashiDetails()

    suspend fun updateData(wallet: String) {
        val newCollectionList = alchemyRepo.getCollection(wallet)
        val oldCollectionList = mashiDetailsDao.getMashiDetails().first()

        val newNames = newCollectionList.map { it.name }.toSet()
        val oldNames = oldCollectionList.map { it.name }.toSet()

        val toAdd = newCollectionList.filter { it.name !in oldNames }

        val toDelete = oldCollectionList.filter { it.name !in newNames }

        if (toAdd.isNotEmpty()) {
            mashiDetailsDao.insertMashiDetails(toAdd.toEntities())
        }
        if (toDelete.isNotEmpty()) {
            mashiDetailsDao.deleteMashiDetails(toDelete)
        }
    }
}