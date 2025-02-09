package com.example.duckit.data.di

import com.example.duckit.BuildConfig.BASE_URL
import com.example.duckit.data.repository.PostRepositoryImpl
import com.example.duckit.data.source.DuckitApi
import com.example.duckit.domain.repository.PostRepository
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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

    @Provides
    @Singleton
    fun providePostRepository(api: DuckitApi): PostRepository =
        PostRepositoryImpl(api)
}
