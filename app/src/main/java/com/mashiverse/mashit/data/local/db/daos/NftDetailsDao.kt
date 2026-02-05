package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.NftDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NftDetailsDao {
    @Query("SELECT * FROM nft_details")
    fun getNftDetails(): Flow<List<NftDetailsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNftDetails(mashiDetails: List<NftDetailsEntity>)

    @Delete
    suspend fun deleteNftDetails(mashiDetails: List<NftDetailsEntity>)
}