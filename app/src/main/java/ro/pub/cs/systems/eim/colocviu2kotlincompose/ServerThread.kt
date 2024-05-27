package ro.pub.cs.systems.eim.colocviu2kotlincompose


import android.util.Log
import java.io.IOException
import java.net.ServerSocket

// todo 11 - Create Server Thread: Create a server thread that listens for client connections and handles them.
class ServerThread(port: Int) : Thread() {

    private var serverSocket: ServerSocket? = null
    private val data: HashMap<String, WeatherForecastInformation> = HashMap()

    init {
        try {
            serverSocket = ServerSocket(port)
        } catch (ioException: IOException) {
            Log.e(TAG, "An exception has occurred: ${ioException.message}")
        }
    }

    @Synchronized
    fun setData(city: String, weatherForecastInformation: WeatherForecastInformation) {
        data[city] = weatherForecastInformation
    }

    @Synchronized
    fun getData(): HashMap<String, WeatherForecastInformation> {
        return data
    }

    override fun run() {
        try {
            while (!currentThread().isInterrupted) {
                Log.i(TAG, "[SERVER THREAD] Waiting for a client invocation...")
                val socket = serverSocket?.accept()
                Log.i(TAG, "[SERVER THREAD] A connection request was received from ${socket?.inetAddress}:${socket?.localPort}")
                val communicationThread = CommunicationThread(this, socket!!)
                communicationThread.start()
            }
        } catch (ioException: IOException) {
            Log.e(TAG, "[SERVER THREAD] An exception has occurred: ${ioException.message}")
        }
    }

    fun stopThread() {
        interrupt()
        serverSocket?.close()
    }

    companion object {
        private const val TAG = "ServerThread"
    }
}
