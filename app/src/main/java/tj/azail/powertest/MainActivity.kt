package tj.azail.powertest

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tj.azail.powertest.BuildConfig.API_KEY
import tj.azail.powertest.data.WeatherApiService
import tj.azail.powertest.data.WeatherRepositoryImpl
import tj.azail.powertest.domain.GetWeatherUseCase
import tj.azail.powertest.domain.WeatherData
import tj.azail.powertest.ui.WeatherUiBuilder
import tj.azail.powertest.ui.WeatherViewModel
import tj.azail.powertest.ui.WeatherViewState

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: ScrollView
    private lateinit var contentLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: WeatherViewModel
    private lateinit var uiBuilder: WeatherUiBuilder

    private val moscowLat = 55.7569
    private val moscowLon = 37.6151

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDependencies()
        initUI()
        observeViewModel()

        viewModel.loadWeather(moscowLat, moscowLon)
    }

    private fun initDependencies() {
        val apiService = WeatherApiService(API_KEY)
        val repository = WeatherRepositoryImpl(apiService)

        val useCase = GetWeatherUseCase(repository)

        viewModel = WeatherViewModel(useCase)
        uiBuilder = WeatherUiBuilder(this)
    }

    private fun initUI() {
        mainLayout = uiBuilder.createMainLayout()
        contentLayout = uiBuilder.createContentLayout()
        progressBar = uiBuilder.createProgressBar()

        contentLayout.addView(progressBar)
        mainLayout.addView(contentLayout)
        setContentView(mainLayout)
    }

    private fun observeViewModel() {
        viewModel.onStateChanged = { state ->
            handleState(state)
        }
    }

    private fun handleState(state: WeatherViewState) {
        when (state) {
            is WeatherViewState.Loading -> showLoading()
            is WeatherViewState.Success -> showWeatherData(state.data)
            is WeatherViewState.Error -> showError(state.message)
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        contentLayout.removeAllViews()
        contentLayout.addView(progressBar)
    }

    private fun showWeatherData(data: WeatherData) {
        progressBar.visibility = View.GONE
        contentLayout.removeAllViews()

        val weatherView = uiBuilder.buildWeatherView(data)
        weatherView.children().forEach { view ->

            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }

            contentLayout.addView(view)
        }
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE

        AlertDialog.Builder(this)
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("Повторить") { _, _ ->
                viewModel.loadWeather(moscowLat, moscowLon)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun LinearLayout.children(): List<View> {
        val list = mutableListOf<View>()
        for (i in 0 until childCount) {
            list.add(getChildAt(i))
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}