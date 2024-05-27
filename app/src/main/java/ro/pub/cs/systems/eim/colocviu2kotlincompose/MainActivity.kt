package ro.pub.cs.systems.eim.colocviu2kotlincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import ro.pub.cs.systems.eim.colocviu2kotlincompose.ui.theme.Colocviu2KotlinComposeTheme

class MainActivity : ComponentActivity() {
    private var serverThread: ServerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Colocviu2KotlinComposeTheme {
                Scaffold {
                    ContentScreen(
                        modifier = Modifier.padding(it),
                        startServer = { port ->
                            serverThread = ServerThread(port)
                            serverThread?.start()
                        },
                        startClient = { address, port, city, informationType, onWeatherInfoReceived ->
                            val clientThread = ClientThread(address, port, city, informationType, onWeatherInfoReceived)
                            clientThread.start()
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serverThread?.stopThread()
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    startServer: (Int) -> Unit,
    startClient: (String, Int, String, String, (WeatherForecastInformation?) -> Unit) -> Unit
) {
    var serverPort by remember { mutableStateOf("") }
    var clientAddress by remember { mutableStateOf("") }
    var clientPort by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var informationType by remember { mutableStateOf("") }
    var weatherInfo by remember { mutableStateOf<WeatherForecastInformation?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Server", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            label = { Text("Type port") },
            placeholder = { Text("Ex: 12345") }
        )
        Button(onClick = {
            val port = serverPort.toIntOrNull()
            if (port != null) {
                startServer(port)
            }
        }) {
            Text(text = "Start Server")
        }

        Text(text = "Client", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = clientAddress,
            onValueChange = { clientAddress = it },
            label = { Text("Type Address") },
            placeholder = { Text("Ex: localhost") }
        )
        OutlinedTextField(
            value = clientPort,
            onValueChange = { clientPort = it },
            label = { Text("Type port") },
            placeholder = { Text("Ex: 12345") }
        )
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Type city") },
            placeholder = { Text("Ex: Bucharest") }
        )
        OutlinedTextField(
            value = informationType,
            onValueChange = { informationType = it },
            label = { Text("Type information type") },
            placeholder = { Text("Ex: temperature") }
        )

        Button(onClick = {
            val port = clientPort.toIntOrNull()
            if (port != null && clientAddress.isNotBlank() && city.isNotBlank() && informationType.isNotBlank()) {
                startClient(clientAddress, port, city, informationType) { weatherInfoResult ->
                    weatherInfo = weatherInfoResult
                }
            }
        }) {
            Text(text = "Start Client")
        }

        weatherInfo?.let {
            Column {
                Text(text = "Temperature: ${it.temperature}")
                Text(text = "Wind Speed: ${it.windSpeed}")
                Text(text = "Condition: ${it.condition}")
                Text(text = "Pressure: ${it.pressure}")
                Text(text = "Humidity: ${it.humidity}")
            }
        }
    }
}


