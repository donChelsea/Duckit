package com.example.duckit.domain.di

import android.content.Context
import com.example.duckit.BuildConfig.BASE_URL
import com.example.duckit.domain.network.ConnectivityObserver
import com.example.duckit.data.network.TokenAuthenticator
import com.example.duckit.domain.network.TokenManager
import com.example.duckit.data.repository.PostRepositoryImpl
import com.example.duckit.data.source.DuckitApi
import com.example.duckit.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun providePostRepository(api: DuckitApi): PostRepository =
        PostRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = ConnectivityObserver(context)

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager = TokenManager(context)
}
