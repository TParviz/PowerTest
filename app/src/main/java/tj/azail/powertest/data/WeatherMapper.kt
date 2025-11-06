package tj.azail.powertest.data

import org.json.JSONObject
import tj.azail.powertest.domain.CurrentWeather
import tj.azail.powertest.domain.DailyWeather
import tj.azail.powertest.domain.HourlyWeather
import tj.azail.powertest.domain.WeatherData
import java.util.Calendar

object WeatherMapper {

    fun mapToWeatherData(json: String): WeatherData {
        val jsonObject = JSONObject(json)

        return WeatherData(
            location = mapLocation(jsonObject),
            current = mapCurrentWeather(jsonObject),
            hourly = mapHourlyWeather(jsonObject),
            daily = mapDailyWeather(jsonObject)
        )
    }

    private fun mapLocation(json: JSONObject): String {
        return json.getJSONObject("location").getString("name")
    }

    private fun mapCurrentWeather(json: JSONObject): CurrentWeather {
        val current = json.getJSONObject("current")
        val condition = current.getJSONObject("condition")

        return CurrentWeather(
            temperature = current.getDouble("temp_c"),
            feelsLike = current.getDouble("feelslike_c"),
            condition = condition.getString("text"),
            windSpeed = current.getDouble("wind_kph"),
            humidity = current.getInt("humidity"),
            pressure = current.getDouble("pressure_mb")
        )
    }

    private fun mapHourlyWeather(json: JSONObject): List<HourlyWeather> {
        val forecastDay = json.getJSONObject("forecast")
            .getJSONArray("forecastday")
            .getJSONObject(0)

        val hours = forecastDay.getJSONArray("hour")
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val hourlyList = mutableListOf<HourlyWeather>()

        for (i in currentHour until hours.length()) {
            val hour = hours.getJSONObject(i)
            val condition = hour.getJSONObject("condition")

            hourlyList.add(
                HourlyWeather(
                    time = hour.getString("time"),
                    temperature = hour.getDouble("temp_c"),
                    condition = condition.getString("text")
                )
            )
        }

        return hourlyList
    }

    private fun mapDailyWeather(json: JSONObject): List<DailyWeather> {
        val forecastDays = json.getJSONObject("forecast")
            .getJSONArray("forecastday")

        val dailyList = mutableListOf<DailyWeather>()

        for (i in 0 until minOf(3, forecastDays.length())) {
            val day = forecastDays.getJSONObject(i)
            val dayData = day.getJSONObject("day")
            val condition = dayData.getJSONObject("condition")

            dailyList.add(
                DailyWeather(
                    date = day.getString("date"),
                    maxTemp = dayData.getDouble("maxtemp_c"),
                    minTemp = dayData.getDouble("mintemp_c"),
                    condition = condition.getString("text"),
                    chanceOfRain = dayData.getInt("daily_chance_of_rain")
                )
            )
        }

        return dailyList
    }
}