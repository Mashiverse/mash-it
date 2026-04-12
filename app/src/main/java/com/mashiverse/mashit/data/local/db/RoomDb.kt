package com.mashiverse.mashit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mashiverse.mashit.data.local.db.converters.NftConverters
import com.mashiverse.mashit.data.local.db.converters.ImageTypeConverters
import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.NftEntity
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity

@Database(
    entities = [NftEntity::class, ImageTypeEntity::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(NftConverters::class, ImageTypeConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun getNftDao(): NftDao
    abstract fun getImageTypeDao(): TraitTypeDao
}