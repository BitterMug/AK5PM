package com.example.ak5pm

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ak5pm.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchTemperatureData().start()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchTemperatureData(): Thread{
        return Thread{
            val url = URL("https://ak5pm.bcview.cz/")
            val connection = url.openConnection() as HttpsURLConnection

            if(connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamRenderer = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamRenderer, Request::class.java)
                updateUI(request)
                inputStreamRenderer.close()
                inputSystem.close()
            }
        }
    }

    private fun updateUI(request: Request){
        runOnUiThread{
            kotlin.run {
                binding.temp.text = String.format("Temperature: %.2f", request.TEMP)
                binding.humid.text = String.format("Humidity: %.2f", request.HUMID)
            }
        }
    }
}