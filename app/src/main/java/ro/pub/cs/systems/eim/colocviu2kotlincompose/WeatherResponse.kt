package ro.pub.cs.systems.eim.colocviu2kotlincompose


import com.google.gson.annotations.SerializedName

// TODO 3.1 - Create Data Models: Create a data model for the weather response.
data class WeatherResponse(
    @SerializedName("weather")
    val weather: List<Weather>,

    @SerializedName("main")
    val main: Main,

    @SerializedName("wind")
    val wind: Wind,

    @SerializedName("name")
    val cityName: String
)

data class Weather(
    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String
)

data class Main(
    @SerializedName("temp")
    val temperature: String,

    @SerializedName("pressure")
    val pressure: String,

    @SerializedName("humidity")
    val humidity: String
)

data class Wind(
    @SerializedName("speed")
    val speed: String
)
