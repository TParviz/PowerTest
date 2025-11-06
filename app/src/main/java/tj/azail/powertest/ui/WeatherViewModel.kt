package tj.azail.powertest.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tj.azail.powertest.domain.GetWeatherUseCase

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    var onStateChanged: ((WeatherViewState) -> Unit)? = null

    fun loadWeather(lat: Double, lon: Double) {
        onStateChanged?.invoke(WeatherViewState.Loading)

        scope.launch {
            val result = getWeatherUseCase(lat, lon)

            result.fold(
                onSuccess = { data ->
                    onStateChanged?.invoke(WeatherViewState.Success(data))
                },
                onFailure = { error ->
                    onStateChanged?.invoke(
                        WeatherViewState.Error(error.message ?: "Ошибка загрузки данных")
                    )
                }
            )
        }
    }

    fun onCleared() {
        scope.coroutineContext[Job]?.cancel()
    }
}