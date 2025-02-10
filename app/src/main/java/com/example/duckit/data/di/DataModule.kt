package com.example.duckit.data.di

import com.example.duckit.BuildConfig.BASE_URL
import com.example.duckit.data.network.TokenAuthenticator
import com.example.duckit.data.source.DuckitApi
import com.example.duckit.domain.network.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): TokenAuthenticator =
        TokenAuthenticator(tokenManager)

    @Provides
    @Singleton
    fun provideOkHttpClient(authenticator: TokenAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideDuckitApi(okHttpClient: OkHttpClient): DuckitApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DuckitApi::class.java)
}
