package com.mashiverse.mashit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.mashiverse.mashit.data.remote.apis.AlchemyApi
import com.mashiverse.mashit.data.remote.apis.MashitApi
import com.mashiverse.mashit.data.remote.apis.MashiverseApi
import com.mashiverse.mashit.utils.ALCHEMY_BASE_URL
import com.mashiverse.mashit.utils.MASHIT_BASE_URL
import com.mashiverse.mashit.utils.MASHIVERSE_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @Named("AlchemyClient")
    fun provideAlchemyClient(): Retrofit = Retrofit
        .Builder()
        .baseUrl(ALCHEMY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAlchemyApi(@Named("AlchemyClient") retrofit: Retrofit): AlchemyApi =
        retrofit.create(AlchemyApi::class.java)

    @Provides
    @Singleton
    @Named("MashItClient")
    fun provideMashItClient(): Retrofit = Retrofit
        .Builder()
        .baseUrl(MASHIT_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    fun provideMashItApi(@Named("MashItClient") retrofit: Retrofit): MashitApi =
        retrofit.create(MashitApi::class.java)

    @Provides
    @Singleton
    @Named("MashiverseClient")
    fun provideMashiClient(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()

        return Retrofit
            .Builder()
            .baseUrl(MASHIVERSE_BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    fun provideMashiverseApi(@Named("MashiverseClient") retrofit: Retrofit) =
        retrofit.create(MashiverseApi::class.java)
}