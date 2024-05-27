package ro.pub.cs.systems.eim.colocviu2kotlincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ro.pub.cs.systems.eim.colocviu2kotlincompose.ui.theme.Colocviu2KotlinComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Colocviu2KotlinComposeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold {
                    paddingValues -> ContentScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

// TODO 1 - implement UI
@Composable
fun ContentScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Server", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        var serverPort by remember { mutableStateOf("") }
        OutlinedTextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            label = { Text("Type port") },
            placeholder = { Text("Ex: 12345") }
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Connect")
        }

        Text(text = "Client", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        var clientAddress by remember { mutableStateOf("") }

        OutlinedTextField(
            value = clientAddress,
            onValueChange = { clientAddress = it },
            label = { Text("Type Address") },
            placeholder = { Text("Ex: localhost") }
        )


        var clientPort by remember { mutableStateOf("") }
        OutlinedTextField(
            value = clientPort,
            onValueChange = { clientPort = it },
            label = { Text("Type here") },
            placeholder = { Text("Ex: 12345") }
        )

        var city by remember { mutableStateOf("") }
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Type city") },
            placeholder = { Text("Ex: Bucharest") }
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Get info")
        }



    }
}