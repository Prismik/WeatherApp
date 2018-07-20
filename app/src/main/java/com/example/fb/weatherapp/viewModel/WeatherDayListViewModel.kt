package com.example.fb.weatherapp.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fb.weatherapp.api.CityItemData
import com.example.fb.weatherapp.api.WeatherForecastHttpService
import com.example.fb.weatherapp.api.WeatherItemData

class WeatherDayListViewModel: ViewModel() {
    private var weatherItems: MutableLiveData<List<WeatherItemData>> = MutableLiveData()
    private var cityItem: MutableLiveData<CityItemData> = MutableLiveData()

    fun weatherItems(): LiveData<List<WeatherItemData>> {
        return weatherItems
    }

    fun cityItem(): LiveData<CityItemData> {
        return cityItem
    }

    fun search(city: CityItemData) {
        cityItem.postValue(city)
        WeatherForecastHttpService.getForecast(city, {
            weatherItems.postValue(it)
        })
    }
}