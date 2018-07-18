package com.example.fb.weatherapp.api

import android.content.Context
import com.example.fb.weatherapp.R
import okhttp3.*
import java.io.IOException
import org.json.JSONArray
import org.json.JSONObject
import okhttp3.HttpUrl





class WeatherForecastHttpService {
    enum class Intervals {
        FiveDays,
        SixteenDays
    }

    companion object {
        val client = OkHttpClient()
        var cities = ArrayList<CityItemData>()

        fun loadData(context: Context) {
            var countries = context.resources.getStringArray(R.array.countries)
            for (country in countries) {
                parseCountry(country)
            }
        }

        private fun parseCountry(country: String) {
            val values = country.split('|')

            cities.add(CityItemData(values[0], values[1]))
        }

        fun getForecast(atCity: CityItemData, callback: (ArrayList<WeatherItemData>) -> Unit) {
            val url = "https://api.openweathermap.org/data/2.5/forecast"
            val httpBuider = HttpUrl.parse(url)!!.newBuilder()
            httpBuider.addQueryParameter("q", "${atCity.name},${atCity.countryCode}")
            httpBuider.addQueryParameter("units", "metric")
            httpBuider.addQueryParameter("APPID", "a53cb4b89bd8f8d995a8fcf302627dbe")
            val request = Request.Builder().url(httpBuider.build()).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    print("Error")
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        callback(parseForecastResponse(response))
                    } else {
                        callback(arrayListOf<WeatherItemData>())
                    }
                }
            })
        }

        private fun parseForecastResponse(response: Response): ArrayList<WeatherItemData> {
            val jsonData = response.body()?.string()
            val jsonObject = JSONObject(jsonData)
            val jsonArray = jsonObject.getJSONArray("list")
            var forecast = arrayListOf<WeatherItemData>()
            for (i in 0 until 8) {
                val forecastItem = jsonArray.getJSONObject(i)
                val utcTime = forecastItem.getLong("dt")
                val temperatureItem = forecastItem.getJSONObject("main")
                val temperature = temperatureItem.getDouble("temp").toString()
                val weatherItem =  forecastItem.getJSONArray("weather").getJSONObject(0)
                val iconId = weatherItem.getString("icon")
                forecast.add(WeatherItemData(utcTime, temperature, iconId))
            }
            return forecast
        }

    }
}