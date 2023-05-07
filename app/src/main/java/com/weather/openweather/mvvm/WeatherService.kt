package com.weather.openweather.mvvm

import com.weather.openweather.mvvm.modelclasses.CommonWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?")
    suspend fun getWeatherReport(@Query("q") q : String, @Query("appid") appid : String): Response<CommonWeather>

}