package com.example.ak5pm

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CurrentFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchTemperatureData().start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("saved_temp", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val saveButton: Button = requireActivity().findViewById(R.id.save_data)
        val savedTempData: TextView = requireActivity().findViewById(R.id.saved_temp_data)
        val savedHumidData: TextView = requireActivity().findViewById(R.id.saved_humid_data)
        val saveCustomButton: Button = requireActivity().findViewById(R.id.save_custom_data)
        val customTempData: TextInputEditText = requireActivity().findViewById(R.id.custom_temp)
        val customHumidData: TextInputEditText = requireActivity().findViewById(R.id.custom_humid)
        val temp: TextView = requireActivity().findViewById(R.id.temp)
        val humid: TextView = requireActivity().findViewById(R.id.humid)

        saveButton.setOnClickListener{
            val temperature = temp.text.toString()
            val humidity = humid.text.toString()

            editor.putString("temp", temperature)
            editor.putString("humid", humidity)

            editor.apply()
        }

        saveCustomButton.setOnClickListener{
            val temperature = customTempData.text.toString()
            val humidity = customHumidData.text.toString()

            editor.putString("temp", temperature)
            editor.putString("humid", humidity)

            editor.apply()
        }

        savedTempData.text = sharedPreferences.getString("temp", null)
        savedHumidData.text = sharedPreferences.getString("humid", null)

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
        activity?.runOnUiThread{
            kotlin.run {
                val temp: TextView = requireActivity().findViewById(R.id.temp)
                val humid: TextView = requireActivity().findViewById(R.id.humid)
                temp.text = String.format("Temperature: %.2f", request.TEMP)
                humid.text = String.format("Humidity: %.2f", request.HUMID)
            }
        }
    }

}