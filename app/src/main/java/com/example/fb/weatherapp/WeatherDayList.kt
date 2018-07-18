package com.example.fb.weatherapp

import android.media.Image
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.fb.weatherapp.api.CityItemData
import com.example.fb.weatherapp.api.WeatherForecastHttpService
import com.example.fb.weatherapp.api.WeatherItemData

class WeatherDayList : AppCompatActivity() {
    private lateinit var cityTextInput: EditText

    private lateinit var cityRecyclerView: RecyclerView
    private var autoCompleteViewAdapter = AutoCompleteAdapter()

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

        cityRecyclerView = findViewById<RecyclerView>(R.id.auto_complete_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = autoCompleteViewAdapter
        }

        loadData()

        cityTextInput = findViewById(R.id.city_text_input)
        cityTextInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val suggestions = autoCompleteSuggestions(it.toString())
                    if (suggestions.isNotEmpty()) {
                        showSuggestions(suggestions)
                    } else {
                        hideSuggestions()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        })
    }

    fun loadData() {
        WeatherForecastHttpService.loadData(this)
    }

    fun showSuggestions(suggestions: List<CityItemData>) {
        cityRecyclerView.visibility = RecyclerView.VISIBLE
        autoCompleteViewAdapter.configure(suggestions)
    }

    fun hideSuggestions() {
        cityRecyclerView.visibility = RecyclerView.INVISIBLE
        autoCompleteViewAdapter.configure(emptyList())
    }

    fun autoCompleteSuggestions(term: String): List<CityItemData> {
        return WeatherForecastHttpService.cities.filter {
            it.name.contains(Regex("^[$term]"))
        }
    }

    fun search(term: String) {
        WeatherForecastHttpService.getForecast(term, {
            data = it
            runOnUiThread {
                viewAdapter.configure(data)
            }
        })
    }
}
