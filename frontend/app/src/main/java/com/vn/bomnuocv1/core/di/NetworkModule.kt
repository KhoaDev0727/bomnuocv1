package com.vn.bomnuocv1.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vn.bomnuocv1.core.network.AuthInterceptor
import com.vn.bomnuocv1.data.remote.api.AuthApiService
import com.vn.bomnuocv1.data.remote.api.DashboardApiService
import com.vn.bomnuocv1.data.remote.api.PricingApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Default local backend emulator URL: 10.0.2.2:8080 (for Android Emulator) or localhost:8080
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePricingApiService(retrofit: Retrofit): PricingApiService {
        return retrofit.create(PricingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardApiService(retrofit: Retrofit): DashboardApiService {
        return retrofit.create(DashboardApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFarmerApiService(retrofit: Retrofit): com.vn.bomnuocv1.data.remote.api.FarmerApiService {
        return retrofit.create(com.vn.bomnuocv1.data.remote.api.FarmerApiService::class.java)
    }
}
