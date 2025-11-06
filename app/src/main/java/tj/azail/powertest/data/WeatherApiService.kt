package tj.azail.powertest.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class WeatherApiService(private val apiKey: String) {

    suspend fun fetchWeather(lat: Double, lon: Double): String {
        return withContext(Dispatchers.IO) {
            val url = "https://api.weatherapi.com/v1/forecast.json?" +
                    "key=$apiKey&q=$lat,$lon&days=3&lang=ru"
            URL(url).readText()
        }
    }
}