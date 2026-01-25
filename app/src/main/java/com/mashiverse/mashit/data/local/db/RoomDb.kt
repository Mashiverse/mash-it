package com.mashiverse.mashit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mashiverse.mashit.data.local.db.converters.MashiConverters
import com.mashiverse.mashit.data.local.db.converters.TraitTypeConverters
import com.mashiverse.mashit.data.local.db.daos.MashiDetailsDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.MashiDetailsEntity
import com.mashiverse.mashit.data.local.db.entities.TraitTypeEntity

@Database(
    entities = [MashiDetailsEntity::class, TraitTypeEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(MashiConverters::class, TraitTypeConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun getMashiDetailsDao(): MashiDetailsDao
    abstract fun getTraitTypeDao(): TraitTypeDao
}