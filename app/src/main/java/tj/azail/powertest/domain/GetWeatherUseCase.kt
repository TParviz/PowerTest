package tj.azail.powertest.domain

class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<WeatherData> {
        return repository.getWeatherData(lat, lon)
    }
}