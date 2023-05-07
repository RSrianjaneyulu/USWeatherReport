package com.weather.openweather.mvvm

import com.weather.openweather.mvvm.modelclasses.CommonWeather
import retrofit2.Response

interface WeatherRepository {
    suspend fun getWeatherReport(city: String, appid: String) : Response<CommonWeather>
}