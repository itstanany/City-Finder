package com.itstanany.cityfinder.data.di

import android.content.Context
import com.itstanany.cityfinder.data.datasource.CityLocalDataSource
import com.itstanany.cityfinder.data.repository.CityRepositoryImpl
import com.itstanany.cityfinder.domain.repository.CityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiDataProvideModule {
  @Provides
  @Singleton
  fun provideCityLocalDataSource(
    @ApplicationContext context: Context
  ): CityLocalDataSource {
    return CityLocalDataSource(context)
  }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DiDataBindModule {
  @Binds
  @Singleton
  abstract fun bindCityRepository(
    cityRepositoryImpl: CityRepositoryImpl
  ): CityRepository
}
