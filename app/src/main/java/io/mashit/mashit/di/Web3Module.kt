package io.mashit.mashit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Web3Module {

    @Provides
    @Singleton
    fun provideAppContext(
        @ApplicationContext ctx: Context
    ) = ctx
}