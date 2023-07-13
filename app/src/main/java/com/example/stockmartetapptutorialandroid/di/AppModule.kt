package com.example.stockmartetapptutorialandroid.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stockmartetapptutorialandroid.data.local.StockDb
import com.example.stockmartetapptutorialandroid.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkhttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideStockAPi(okHttpClient: OkHttpClient) : StockApi {
        return Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideStockDb(app: Application) : StockDb {
        return Room.databaseBuilder(
            app,
            StockDb::class.java,
            "stockdb"
        ).build()
    }
}