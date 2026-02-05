package com.mashiverse.mashit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mashiverse.mashit.data.local.db.converters.NftConverters
import com.mashiverse.mashit.data.local.db.converters.ImageTypeConverters
import com.mashiverse.mashit.data.local.db.daos.NftDetailsDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.NftDetailsEntity
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity

@Database(
    entities = [NftDetailsEntity::class, ImageTypeEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(NftConverters::class, ImageTypeConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun getNftDetailsDao(): NftDetailsDao
    abstract fun getImageTypeDao(): TraitTypeDao
}