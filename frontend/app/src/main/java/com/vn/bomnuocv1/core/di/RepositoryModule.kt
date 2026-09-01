package com.vn.bomnuocv1.core.di

import com.vn.bomnuocv1.data.repository.AuthRepositoryImpl
import com.vn.bomnuocv1.data.repository.DashboardRepositoryImpl
import com.vn.bomnuocv1.data.repository.PricingRepositoryImpl
import com.vn.bomnuocv1.domain.repository.AuthRepository
import com.vn.bomnuocv1.domain.repository.DashboardRepository
import com.vn.bomnuocv1.domain.repository.PricingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPricingRepository(
        pricingRepositoryImpl: PricingRepositoryImpl
    ): PricingRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository
}

