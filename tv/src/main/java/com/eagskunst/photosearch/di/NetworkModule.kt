package com.eagskunst.photosearch.di

import android.content.Context
import com.eagskunst.photosearch.commons.ApiKeyQueryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val OKHTTP_CACHE_FILE_NAME = "okhttp_cache"
        const val CACHE_SIZE = 1_000_000L
    }

    @Provides
    fun provideOkHttpLoggingFactory() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideOkHttpCacheFile(@ApplicationContext context: Context) = Cache(
        directory = File(context.cacheDir, OKHTTP_CACHE_FILE_NAME),
        maxSize = CACHE_SIZE
    )

    @Provides
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        cacheFile: Cache,
        apiKeyQueryInterceptor: ApiKeyQueryInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(apiKeyQueryInterceptor)
        .readTimeout(90, TimeUnit.SECONDS)
        .connectTimeout(90, TimeUnit.SECONDS)
        .cache(cacheFile)
        .build()
}