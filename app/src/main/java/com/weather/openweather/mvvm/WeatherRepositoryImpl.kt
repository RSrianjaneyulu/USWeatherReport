package com.weather.openweather.mvvm

import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherService: WeatherService): WeatherRepository {
    override suspend fun getWeatherReport(city: String, appid: String) = weatherService.getWeatherReport(city, appid)
}