package com.mashiverse.mashit.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.mashiverse.mashit.data.local.db.RoomDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext ctx: Context
    ) = Room.databaseBuilder(
        context = ctx,
        klass = RoomDb::class.java,
        name = "mash_it_db"
    ).build()

    @Provides
    fun provideMashiDetailsDao(
        db: RoomDb
    ) = db.getMashiDetailsDao()

    @Provides
    fun provideTraitTypeDao(
        db: RoomDb
    ) = db.getTraitTypeDao()
}