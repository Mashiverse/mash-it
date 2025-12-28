package dev.tymoshenko.mashit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tymoshenko.mashit.data.remote.apis.AlchemyApi
import dev.tymoshenko.mashit.utils.ALCHEMY_BASE_URL
import jakarta.inject.Named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @Named("AlchemyClient")
    fun provideAlchemyClient() = Retrofit
        .Builder()
        .baseUrl(ALCHEMY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAlchemyApi(@Named("AlchemyClient") retrofit: Retrofit): AlchemyApi =
        retrofit.create(AlchemyApi::class.java)

}