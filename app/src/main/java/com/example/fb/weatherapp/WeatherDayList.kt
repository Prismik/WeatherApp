package com.example.fb.weatherapp

import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.fb.weatherapp.api.WeatherItemData

class WeatherDayList : AppCompatActivity() {
    private lateinit var daysRecyclerView: RecyclerView
    private var viewAdapter = WeatherDayAdapter()
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var data: MutableList<WeatherItemData> = ArrayList<WeatherItemData>()

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
        data = mutableListOf(
                WeatherItemData("Monday", "27 celcius", null),
                WeatherItemData("Tuesday", "25 celcius", null),
                WeatherItemData("Wednesday", "22 celcius", null),
                WeatherItemData("Tuesday", "23 celcius", null),
                WeatherItemData("Friday", "21 celcius", null))

        viewAdapter.configure(data)
    }
}