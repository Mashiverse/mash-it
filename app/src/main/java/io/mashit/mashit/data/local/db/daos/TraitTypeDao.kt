package io.mashit.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.mashit.mashit.data.local.db.entities.TraitTypeEntity

@Dao
interface TraitTypeDao {
    @Query("SELECT * FROM trait_types WHERE url = :url")
    fun getTraitTypeEntity(url: String): TraitTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraitType(traitTypeEntity: TraitTypeEntity)

    @Delete
    suspend fun deleteMashiDetails(traitTypeEntity: TraitTypeEntity)
}