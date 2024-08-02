package com.example.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.example.myweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//e877c904be3656b51abe60a2ed583008
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeather("Kanpur")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeather(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }


        })
    }

    private fun fetchWeather(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response =
            retrofit.getWeatherData(cityName, "e877c904be3656b51abe60a2ed583008", "metric")
        response.enqueue(object : Callback<Myweather> {
            override fun onResponse(call: Call<Myweather>, response: Response<Myweather>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxtemp.text = "Max Temp: $maxTemp °C"
                    binding.mintemp.text = "Min Temp: $minTemp °C"
                    binding.humidity.text = "$humidity"
                    binding.wind.text = "$windSpeed"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text = "${time(sunset)}"
                    binding.sea.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.location.text = "$cityName"
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()

                }
            }

            override fun onFailure(call: Call<Myweather>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
}