package com.example.fb.weatherapp.api

import android.media.Image

class WeatherItemData(day: String, temperature: String, image: Image?) {
    val day: String
    val temperature: String
    val image: Image?

    init {
        this.day = day
        this.temperature = temperature
        this.image = image
    }
}