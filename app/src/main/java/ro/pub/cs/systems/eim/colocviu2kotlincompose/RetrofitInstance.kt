package ro.pub.cs.systems.eim.colocviu2kotlincompose

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.BASE_URL

// TODO 5 - Create Retrofit Instance: Create a Retrofit instance to use for the API service.
object RetrofitInstance {
    // TODO 5.1 - make sure is the correct base url
    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WeatherApiService::class.java)
    }
}
