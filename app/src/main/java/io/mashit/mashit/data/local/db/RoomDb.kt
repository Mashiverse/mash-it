package io.mashit.mashit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import io.mashit.mashit.data.local.db.converters.MashiConverters
import io.mashit.mashit.data.local.db.daos.MashiDetailsDao
import io.mashit.mashit.data.local.db.entities.MashiDetailsEntity

@Database(
    entities = [MashiDetailsEntity::class],
    version = 1
)
@TypeConverters(MashiConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun getMashiDetailsDao(): MashiDetailsDao
}