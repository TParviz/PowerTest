package tj.azail.powertest.domain

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, lon: Double): Result<WeatherData>
}