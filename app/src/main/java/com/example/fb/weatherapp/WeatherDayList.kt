package com.example.fb.weatherapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.fb.weatherapp.api.CityItemData
import com.example.fb.weatherapp.api.WeatherForecastHttpService
import com.example.fb.weatherapp.api.WeatherItemData
import com.example.fb.weatherapp.viewModel.WeatherDayListViewModel

class WeatherDayList: AppCompatActivity(), AutoCompleteAdapterDelegate {
    private lateinit var cityTextInput: EditText

    private lateinit var cityRecyclerView: RecyclerView
    private var autoCompleteViewAdapter = AutoCompleteAdapter()

    private lateinit var daysRecyclerView: RecyclerView
    private var viewAdapter = WeatherDayAdapter()

    private var viewModel: WeatherDayListViewModel? = null

    private var handleTextChange: Boolean = true

    private val QUERY_KEY = "query"
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(QUERY_KEY, cityTextInput.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        WeatherForecastHttpService.loadData(this)
        savedInstanceState?.let {
            cityTextInput.setText(it.getString(QUERY_KEY))
        }

    }
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

        viewModel = ViewModelProviders.of(this).get(WeatherDayListViewModel::class.java)
        viewModel?.cityItem()?.observe(this, Observer<CityItemData> {
            it?.let { city ->
                handleTextChange = false
                cityTextInput.setText(city.name)
            }
        })
        viewModel?.weatherItems()?.observe(this, Observer<List<WeatherItemData>> {
            it?.let { weatherItems ->
                runOnUiThread {
                    viewAdapter.configure(weatherItems)
                    Keyboard.hide(this, this.currentFocus ?: View(this))
                    cityTextInput.clearFocus()
                }
            }
        })

        WeatherForecastHttpService.loadData(this)

        cityTextInput = findViewById(R.id.city_text_input)
        cityTextInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.toString().isNotBlank() && handleTextChange) {
                        val suggestions = autoCompleteSuggestions(it.toString().toLowerCase())
                        if (suggestions.isNotEmpty()) {
                            showSuggestions(suggestions)
                        } else {
                            hideSuggestions()
                        }
                    }

                    handleTextChange = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        })
    }

    override fun didSelectItem(item: CityItemData) {
        viewModel?.search(item)
        hideSuggestions()
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
}
