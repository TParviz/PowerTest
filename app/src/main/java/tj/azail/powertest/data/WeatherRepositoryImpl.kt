package tj.azail.powertest.data

import tj.azail.powertest.domain.WeatherData
import tj.azail.powertest.domain.WeatherRepository

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lon: Double): Result<WeatherData> {
        return try {
            val response = apiService.fetchWeather(lat, lon)
            val weatherData = WeatherMapper.mapToWeatherData(response)
            Result.success(weatherData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
