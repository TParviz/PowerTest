package tj.azail.powertest.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*
import tj.azail.powertest.domain.CurrentWeather
import tj.azail.powertest.domain.DailyWeather
import tj.azail.powertest.domain.HourlyWeather
import tj.azail.powertest.domain.WeatherData

class WeatherUiBuilder(private val context: Context) {

    fun createMainLayout(): ScrollView {
        return ScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun createContentLayout(): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(32, 32, 32, 32)
        }
    }

    fun createProgressBar(): ProgressBar {
        return ProgressBar(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                topMargin = 200
            }
            visibility = View.GONE
        }
    }

    fun buildWeatherView(data: WeatherData): LinearLayout {
        val layout = createContentLayout()

        layout.addView(createTextView(data.location, 32f, true))
        layout.addView(createDivider())

        layout.addView(createTextView("Текущая погода", 24f, true))
        layout.addView(createCurrentWeatherCard(data.current))
        layout.addView(createDivider())

        layout.addView(createTextView("Почасовой прогноз", 24f, true))
        layout.addView(createHourlyForecast(data.hourly))
        layout.addView(createDivider())

        layout.addView(createTextView("Прогноз на 3 дня", 24f, true))
        data.daily.forEach { day ->
            layout.addView(createDailyWeatherCard(day))
        }

        return layout
    }

    private fun createCurrentWeatherCard(current: CurrentWeather): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            setPadding(24, 24, 24, 24)
            setBackgroundColor(0xFFE3F2FD.toInt())

            addView(createTextView("🌡️ ${current.temperature.toInt()}°C", 48f, true))
            addView(createTextView(current.condition, 20f, false))
            addView(createTextView("Ощущается как: ${current.feelsLike.toInt()}°C", 16f, false))
            addView(createTextView("💨 Ветер: ${current.windSpeed.toInt()} км/ч", 16f, false))
            addView(createTextView("💧 Влажность: ${current.humidity}%", 16f, false))
            addView(createTextView("🌡️ Давление: ${current.pressure.toInt()} мбар", 16f, false))
        }
    }

    private fun createHourlyForecast(hourly: List<HourlyWeather>): HorizontalScrollView {
        val scrollView = HorizontalScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 16, 0, 16)
        }

        hourly.take(12).forEach { hour ->
            container.addView(createHourlyCard(hour))
        }

        scrollView.addView(container)
        return scrollView
    }

    private fun createHourlyCard(hour: HourlyWeather): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }
            setPadding(16, 16, 16, 16)
            setBackgroundColor(0xFFFFF3E0.toInt())
            gravity = Gravity.CENTER

            val time = hour.time.substring(11, 16)
            addView(createTextView(time, 14f, true))
            addView(createTextView(WeatherUtils.getWeatherEmoji(hour.condition), 24f, false))
            addView(createTextView("${hour.temperature.toInt()}°", 16f, false))
        }
    }

    private fun createDailyWeatherCard(day: DailyWeather): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            setPadding(24, 16, 24, 16)
            setBackgroundColor(0xFFF1F8E9.toInt())

            val topRow = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            topRow.addView(createTextView(WeatherUtils.formatDate(day.date), 16f, true).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            topRow.addView(createTextView(WeatherUtils.getWeatherEmoji(day.condition), 24f, false))
            topRow.addView(createTextView("${day.maxTemp.toInt()}° / ${day.minTemp.toInt()}°", 16f, false).apply {
                setPadding(16, 0, 0, 0)
            })

            addView(topRow)
            addView(createTextView(day.condition, 14f, false).apply {
                setPadding(0, 4, 0, 0)
            })

            if (day.chanceOfRain > 0) {
                addView(createTextView("☔ Вероятность дождя: ${day.chanceOfRain}%", 14f, false).apply {
                    setPadding(0, 4, 0, 0)
                    setTextColor(0xFF1976D2.toInt())
                })
            }
        }
    }

    private fun createTextView(text: String, size: Float, bold: Boolean): TextView {
        return TextView(context).apply {
            this.text = text
            textSize = size
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 4, 0, 4)
            }
        }
    }

    private fun createDivider(): View {
        return View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            ).apply {
                setMargins(0, 24, 0, 24)
            }
            setBackgroundColor(0xFFCCCCCC.toInt())
        }
    }
}