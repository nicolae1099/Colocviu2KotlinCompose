package ro.pub.cs.systems.eim.colocviu2kotlincompose


//TODO 3 - Create Data Models: Create a data model for the weather forecast.
data class WeatherForecastInformation(
    val temperature: String?,
    val windSpeed: String?,
    val condition: String?,
    val pressure: String?,
    val humidity: String?
)
