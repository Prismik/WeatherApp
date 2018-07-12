package com.example.fb.weatherapp

import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.fb.weatherapp.api.WeatherForecastHttpService
import com.example.fb.weatherapp.api.WeatherItemData

class WeatherDayList : AppCompatActivity() {
    private lateinit var daysRecyclerView: RecyclerView
    private var viewAdapter = WeatherDayAdapter()
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var data: List<WeatherItemData> = ArrayList<WeatherItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_day_list)

        viewManager = LinearLayoutManager(this)
        daysRecyclerView = findViewById<RecyclerView>(R.id.weatherDaysRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        loadData()
    }

    fun loadData() {
        WeatherForecastHttpService.loadData(this)
        WeatherForecastHttpService.getForecast("Montreal", {
            data = it
            runOnUiThread {
                viewAdapter.configure(data)
            }
        })
    }
}
