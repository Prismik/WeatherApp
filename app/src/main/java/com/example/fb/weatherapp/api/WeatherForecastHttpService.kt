package com.example.fb.weatherapp.api

import okhttp3.OkHttpClient
import okhttp3.Request

class WeatherForecastHttpService {
    enum class Locations(city: String, countryCode: String) {
        Montreal("Montreal", "CA")
    }

    enum class Intervals {
        FiveDays,
        SixteenDays
    }

    companion object {
        val client = OkHttpClient()

        fun getForecast(at: Locations, duration: Intervals): ArrayList<WeatherItemData> {
            val url = "api.openweathermap.org/data/2.5/forecast?q=" + at + at
            val request = Request.Builder()
                    .url(url)
                    .build()

            //client.newCall(request).enqueue(object : Callback {
            //    override fun onFailure(call: Call, e: IOException) {}
            //    override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
            //})
            return arrayListOf<WeatherItemData>()
        }


    }
}