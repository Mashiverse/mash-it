package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.TraitTypeEntity
import javax.inject.Inject

class TraitTypeRepo @Inject constructor(
    val traitTypeDao: TraitTypeDao
) {
    fun getTraitTypeEntity(url: String): TraitTypeEntity? = traitTypeDao.getTraitTypeEntity(url)

    suspend fun insertTraitType(traitTypeEntity: TraitTypeEntity) = traitTypeDao.insertTraitType(traitTypeEntity)
}