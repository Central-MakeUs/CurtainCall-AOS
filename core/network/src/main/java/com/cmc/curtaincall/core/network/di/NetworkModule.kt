package com.cmc.curtaincall.core.network.di

import com.cmc.curtaincall.core.network.BuildConfig
import com.cmc.curtaincall.core.network.interceptors.CurtainCallInterceptor
import com.cmc.curtaincall.core.network.qualifiers.LoggingClient
import com.cmc.curtaincall.core.network.qualifiers.LoggingRetrofit
import com.cmc.curtaincall.core.network.qualifiers.RefreshTokenClient
import com.cmc.curtaincall.core.network.qualifiers.RefreshTokenRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = BuildConfig.CURTAIN_CALL_BASE_URL

    @LoggingRetrofit
    @Provides
    @Singleton
    fun provideLoggingRetrofit(
        @LoggingClient okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @RefreshTokenRetrofit
    @Provides
    @Singleton
    fun provideRefreshTokenRetrofit(
        @RefreshTokenClient okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @RefreshTokenClient
    @Provides
    @Singleton
    fun provideRefreshTokenClient(
        curtainCallInterceptor: CurtainCallInterceptor,
        loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(curtainCallInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @LoggingClient
    @Provides
    @Singleton
    fun provideLoggingClient(
        loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
}
