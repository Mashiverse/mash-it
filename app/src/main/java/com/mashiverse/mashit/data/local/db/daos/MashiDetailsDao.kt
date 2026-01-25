package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.MashiDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MashiDetailsDao {
    @Query("SELECT * FROM mashi_details")
    fun getMashiDetails(): Flow<List<MashiDetailsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMashiDetails(mashiDetails: List<MashiDetailsEntity>)

    @Delete
    suspend fun deleteMashiDetails(mashiDetails: List<MashiDetailsEntity>)
}