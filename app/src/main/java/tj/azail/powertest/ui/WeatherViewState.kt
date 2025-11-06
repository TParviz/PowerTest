package tj.azail.powertest.ui

import tj.azail.powertest.domain.WeatherData

sealed class WeatherViewState {
    data object Loading : WeatherViewState()
    data class Success(val data: WeatherData) : WeatherViewState()
    data class Error(val message: String) : WeatherViewState()
}