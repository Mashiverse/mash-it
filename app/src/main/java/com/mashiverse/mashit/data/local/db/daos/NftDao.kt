package com.mashiverse.mashit.data.local.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mashiverse.mashit.data.local.db.entities.NftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NftDao {
    @Query("SELECT * FROM nft_details WHERE name = :name LIMIT 1")
    suspend fun getNftByName(name: String): NftEntity?

    @Query("SELECT * FROM nft_details WHERE isOwned=1")
    fun getOwnedNfts(): Flow<List<NftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNft(nfts: NftEntity)

    @Delete
    suspend fun deleteNfts(nfts: List<NftEntity>)
}