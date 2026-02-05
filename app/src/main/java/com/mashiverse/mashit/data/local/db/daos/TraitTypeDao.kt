package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity

@Dao
interface TraitTypeDao {
    @Query("SELECT * FROM trait_types WHERE url = :url")
    fun getImageType(url: String): ImageTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageType(imageTypeEntity: ImageTypeEntity)

    @Delete
    suspend fun deleteImageType(imageTypeEntity: ImageTypeEntity)
}