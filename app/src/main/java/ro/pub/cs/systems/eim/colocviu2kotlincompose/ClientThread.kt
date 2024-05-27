package ro.pub.cs.systems.eim.colocviu2kotlincompose

import android.util.Log
import java.io.IOException
import java.net.Socket
import org.json.JSONObject

// todo 10 - Create Client Thread: Create a client thread that sends a request to the server and receives the response.
class ClientThread(
    private val address: String,
    private val port: Int,
    private val city: String,
    private val informationType: String,
    private val onWeatherInfoReceived: (WeatherForecastInformation?) -> Unit
) : Thread() {

    private var socket: Socket? = null

    override fun run() {
        try {
            socket = Socket(address, port)
            val bufferedReader = Utils.getReader(socket!!)
            val printWriter = Utils.getWriter(socket!!)

            printWriter.println(city)
            printWriter.println(informationType)

            val weatherInfoString = bufferedReader.readLine()
            val weatherInfo = weatherInfoString?.let {
                try {
                    val jsonObject = JSONObject(it)
                    WeatherForecastInformation(
                        temperature = jsonObject.optString(Constants.TEMPERATURE),
                        windSpeed = jsonObject.optString(Constants.WIND_SPEED),
                        condition = jsonObject.optString(Constants.CONDITION),
                        pressure = jsonObject.optString(Constants.PRESSURE),
                        humidity = jsonObject.optString(Constants.HUMIDITY)
                    )
                } catch (e: Exception) {
                    null
                }
            }

            onWeatherInfoReceived(weatherInfo)

        } catch (ioException: IOException) {
            Log.e(TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
            onWeatherInfoReceived(null)
        } finally {
            try {
                socket?.close()
            } catch (ioException: IOException) {
                Log.e(TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
            }
        }
    }

    companion object {
        private const val TAG = "ro.pub.cs.systems.eim.colocviu2kotlincompose.ClientThread"
    }
}
