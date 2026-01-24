package io.mashit.mashit.data.repos

import io.mashit.mashit.data.local.db.daos.TraitTypeDao
import io.mashit.mashit.data.local.db.entities.TraitTypeEntity
import javax.inject.Inject

class TraitTypeRepo @Inject constructor(
    val traitTypeDao: TraitTypeDao
) {
    fun getTraitTypeEntity(url: String): TraitTypeEntity? = traitTypeDao.getTraitTypeEntity(url)

    suspend fun insertTraitType(traitTypeEntity: TraitTypeEntity) = traitTypeDao.insertTraitType(traitTypeEntity)
}