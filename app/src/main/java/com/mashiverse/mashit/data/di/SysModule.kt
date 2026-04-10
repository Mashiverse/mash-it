package com.mashiverse.mashit.data.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SysModule {

    @Provides
    @Singleton
    fun provideWorker(
        @ApplicationContext context: Context
    ) =  WorkManager.getInstance(context)
}