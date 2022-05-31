package com.example.myapplication
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
class MainActivity : ComponentActivity() {
    val carurl="https://www.streetcar.org/wp-content/uploads/sfmsr/streetcars/uploads/1080-ferrario.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
        downloadFun()

    }
    fun downloadFun(){
        GlobalScope.launch {
            withContext(Dispatchers.IO){

                val url: URL = URL(carurl)
                var mycon: HttpURLConnection =url.openConnection() as HttpURLConnection
                if ( mycon != null){
                    mycon.allowUserInteraction=false
                    mycon.instanceFollowRedirects =true
                    mycon.requestMethod="GET"
                    mycon.connect()
                    if (mycon.responseCode == HTTP_OK){
                        var bmp: Bitmap = BitmapFactory.decodeStream(mycon.inputStream)
                        img= bmp.asImageBitmap()
                        imgready.value=true
                    }
                }
                // delay(5000)
            }

            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext,"test",Toast.LENGTH_LONG).show()

            }

        }
    }

}


lateinit var img: ImageBitmap
var imgready = mutableStateOf<Boolean>(false)
@Composable
fun Greeting(name: String) {
    Column() {
        SimpleTextField()
        Button(onClick = { /*TODO*/ }) {
            Text("按我")
        }
        if ( imgready.value) {
            Image(painter = BitmapPainter(img), contentDescription = "image",
                modifier = Modifier.fillMaxSize())
        }
    }

// Text(text = "Hello $name!")
}

@Composable
fun SimpleTextField() {
    var  text = remember {
        mutableStateOf<TextFieldValue>(TextFieldValue(""))
    }
    TextField(
        value = text.value,
        onValueChange = { newText ->
            text.value = newText
        }
    )
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}