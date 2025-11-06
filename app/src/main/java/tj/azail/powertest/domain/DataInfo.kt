package tj.azail.powertest.domain

// Data классы
data class WeatherData(
    val location: String,
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)

data class CurrentWeather(
    val temperature: Double,
    val feelsLike: Double,
    val condition: String,
    val windSpeed: Double,
    val humidity: Int,
    val pressure: Double
)

data class HourlyWeather(
    val time: String,
    val temperature: Double,
    val condition: String
)

data class DailyWeather(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val condition: String,
    val chanceOfRain: Int
)