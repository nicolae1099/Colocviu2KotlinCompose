package ro.pub.cs.systems.eim.colocviu2kotlincompose

// TODO 4 - Create Retrofit Service: Define a Retrofit service interface for the weather API.

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    // TODO 4.1 - make sure is the correct endpoint
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("APPID") apiKey: String,
        @Query("units") units: String
    ): WeatherResponse
}
