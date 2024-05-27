package ro.pub.cs.systems.eim.colocviu2kotlincompose

import android.util.Log
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.ALL
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.API_KEY
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.CONDITION
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.HUMIDITY
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.PRESSURE
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.TEMPERATURE
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.UNITS
import ro.pub.cs.systems.eim.colocviu2kotlincompose.Constants.WIND_SPEED
import java.io.IOException
import java.net.Socket

// todo 12 - Create Communication Thread: Create a communication thread that processes the client request and sends the response.
class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket) : Thread() {

    override fun run() {
        try {
            val bufferedReader = Utils.getReader(socket)
            val printWriter = Utils.getWriter(socket)

            Log.i(TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)")
            val city = bufferedReader.readLine()
            val informationType = bufferedReader.readLine()
            if (city.isNullOrEmpty() || informationType.isNullOrEmpty()) {
                Log.e(TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)")
                return
            }

            val data = serverThread.getData()
            var weatherForecastInformation: WeatherForecastInformation? = data[city]
            if (weatherForecastInformation == null) {
                Log.i(TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...")

                // Call the Retrofit service inside a coroutine
                val response = runBlocking {
                    try {
                        RetrofitInstance.api.getWeather(city, API_KEY, UNITS)
                    } catch (exception: Exception) {
                        Log.e(TAG, "[COMMUNICATION THREAD] Error during web service call: ${exception.message}")
                        null
                    }
                }

                response?.let {
                    val condition = response.weather[0].description

                    val main = response.main
                    val temperature = main.temperature
                    val pressure = main.pressure
                    val humidity = main.humidity
                    val wind = response.wind
                    val windSpeed = wind.speed

                    val newWeatherForecastInformation = WeatherForecastInformation(
                        temperature, windSpeed, condition, pressure, humidity
                    )
                    serverThread.setData(city, newWeatherForecastInformation)
                    weatherForecastInformation = newWeatherForecastInformation
                }
            }

            val json = JSONObject()

            when (informationType) {
                ALL -> {
                    json.put(TEMPERATURE, weatherForecastInformation?.temperature)
                    json.put(WIND_SPEED, weatherForecastInformation?.windSpeed)
                    json.put(CONDITION, weatherForecastInformation?.condition)
                    json.put(PRESSURE, weatherForecastInformation?.pressure)
                    json.put(HUMIDITY, weatherForecastInformation?.humidity)
                }
                TEMPERATURE -> json.put(TEMPERATURE, weatherForecastInformation?.temperature)
                WIND_SPEED -> json.put(WIND_SPEED, weatherForecastInformation?.windSpeed)
                CONDITION -> json.put(CONDITION, weatherForecastInformation?.condition)
                HUMIDITY -> json.put(HUMIDITY, weatherForecastInformation?.humidity)
                PRESSURE -> json.put(PRESSURE, weatherForecastInformation?.pressure)
                else -> Log.e(TAG, "[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!")
            }

            Log.i(TAG, "[COMMUNICATION THREAD] Sending the information to the client: $json")
            printWriter.println(json.toString())
            printWriter.flush()

        } catch (ioException: IOException) {
            Log.e(TAG, "[COMMUNICATION THREAD] An exception has occurred: ${ioException.message}")
        } finally {
            try {
                socket.close()
            } catch (ioException: IOException) {
                Log.e(TAG, "[COMMUNICATION THREAD] An exception has occurred: ${ioException.message}")
            }
        }
    }

    companion object {
        private const val TAG = "ro.pub.cs.systems.eim.colocviu2kotlincompose.CommunicationThread"
    }
}
