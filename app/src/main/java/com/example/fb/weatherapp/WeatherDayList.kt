package com.example.fb.weatherapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.fb.weatherapp.api.CityItemData
import com.example.fb.weatherapp.api.WeatherForecastHttpService
import com.example.fb.weatherapp.api.WeatherItemData

class WeatherDayList: AppCompatActivity(), AutoCompleteAdapterDelegate {
    private lateinit var cityTextInput: EditText

    private lateinit var cityRecyclerView: RecyclerView
    private var autoCompleteViewAdapter = AutoCompleteAdapter()

    private lateinit var daysRecyclerView: RecyclerView
    private var viewAdapter = WeatherDayAdapter()

    private var data: List<WeatherItemData> = ArrayList<WeatherItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_day_list)

        val context = this
        daysRecyclerView = findViewById<RecyclerView>(R.id.weatherDaysRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        autoCompleteViewAdapter.delegate = this
        cityRecyclerView = findViewById<RecyclerView>(R.id.auto_complete_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = autoCompleteViewAdapter
            alpha = 0.toFloat()
        }

        loadData()

        cityTextInput = findViewById(R.id.city_text_input)
        cityTextInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.toString().isNotBlank()) {
                        val suggestions = autoCompleteSuggestions(it.toString().toLowerCase())
                        if (suggestions.isNotEmpty()) {
                            showSuggestions(suggestions)
                        } else {
                            hideSuggestions()
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        })
    }

    override fun didSelectItem(item: CityItemData) {
        search(item)
        cityTextInput.setText(item.name)
        hideSuggestions()
    }

    fun loadData() {
        WeatherForecastHttpService.loadData(this)
    }

    fun showSuggestions(suggestions: List<CityItemData>) {
        cityRecyclerView.visibility = RecyclerView.VISIBLE
        daysRecyclerView.visibility = RecyclerView.INVISIBLE
        autoCompleteViewAdapter.configure(suggestions)
        cityRecyclerView.animate().alpha(1.toFloat()).duration = 300
        daysRecyclerView.animate().alpha(0.toFloat()).duration = 300
    }

    fun hideSuggestions() {
        cityRecyclerView.visibility = RecyclerView.INVISIBLE
        daysRecyclerView.visibility = RecyclerView.VISIBLE
        autoCompleteViewAdapter.configure(emptyList())
        cityRecyclerView.animate().alpha(0.toFloat()).duration = 300
        daysRecyclerView.animate().alpha(1.toFloat()).duration = 300
    }

    fun autoCompleteSuggestions(term: String): List<CityItemData> {
        return WeatherForecastHttpService.cities.filter {
            it.name.toLowerCase().contains(Regex("^$term"))
        }
    }

    fun search(city: CityItemData) {
        WeatherForecastHttpService.getForecast(city, {
            data = it
            runOnUiThread {
                viewAdapter.configure(data)
                Keyboard.hide(this, this.currentFocus ?: View(this))
                cityTextInput.clearFocus()
            }
        })
    }
}
