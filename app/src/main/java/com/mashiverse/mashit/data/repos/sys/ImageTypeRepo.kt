package com.mashiverse.mashit.data.repos.sys

import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import javax.inject.Inject

class ImageTypeRepo @Inject constructor(
    val imageTypeDao: TraitTypeDao
) {
    fun getImageType(url: String): ImageTypeEntity? = imageTypeDao.getImageType(url)

    suspend fun insertImageType(imageTypeEntity: ImageTypeEntity) = imageTypeDao.insertImageType(imageTypeEntity)
}