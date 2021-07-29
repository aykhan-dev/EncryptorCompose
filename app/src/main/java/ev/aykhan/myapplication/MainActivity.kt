package ev.aykhan.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ev.aykhan.myapplication.ui.theme.ComposePracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    EncryptorApp()
                }
            }
        }
    }
}

@Composable
fun EncryptorApp() {

    val navController = rememberNavController()

    NavHost(navController = navController, "home") {
        composable("home") { HomePage(navController) }
    }

}

@SuppressLint("NewApi")
@Composable
fun HomePage(navController: NavHostController) {

    val context = LocalContext.current

    val input = remember { mutableStateOf("") }
    val encryption = remember { mutableStateOf<ByteArray?>(null) }

    Column(modifier = Modifier.padding(32.dp)) {

        TextField(
            value = input.value,
            onValueChange = {
                input.value = it
            },
            label = { Text(text = "Type something to encrypt") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "Encryption result is ${encryption.value}. Click to see decrypted version",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                    val decryption = SecureElement.decrypt(encryption.value ?: return@clickable)
                    Toast
                        .makeText(
                            context,
                            String(decryption ?: ByteArray(0), Charsets.UTF_8),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                },
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        Button(
            onClick = { encryption.value = SecureElement.encrypt(input.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Encrypt and save")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePracticeTheme {
        EncryptorApp()
    }
}