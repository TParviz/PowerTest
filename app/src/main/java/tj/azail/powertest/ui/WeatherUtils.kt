package tj.azail.powertest.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherUtils {

    fun getWeatherEmoji(condition: String): String {
        return when {
            condition.contains("ясно", true) || condition.contains("солнечно", true) -> "☀️"
            condition.contains("облачно", true) || condition.contains("пасмурно", true) -> "☁️"
            condition.contains("дождь", true) || condition.contains("ливень", true) -> "🌧️"
            condition.contains("снег", true) -> "❄️"
            condition.contains("гроза", true) || condition.contains("молния", true) -> "⛈️"
            condition.contains("туман", true) || condition.contains("дымка", true) -> "🌫️"
            condition.contains("переменная", true) -> "⛅"
            else -> "🌤️"
        }
    }

    fun formatDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM, EEEE", Locale("ru"))
        val date = inputFormat.parse(dateStr)
        return outputFormat.format(date ?: Date())
    }
}
