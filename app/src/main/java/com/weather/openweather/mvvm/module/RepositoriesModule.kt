package com.weather.openweather.mvvm.module

import com.weather.openweather.mvvm.WeatherRepository
import com.weather.openweather.mvvm.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {

    @Binds
    fun weatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl) : WeatherRepository
}