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
        var countryKeyCodesMap = HashMap<String, String>()

        fun loadData(context: Context) {
            var countries = context.resources.getStringArray(R.array.countries)
            for (country in countries) {
                parseCountry(country)
            }
        }

        private fun parseCountry(country: String) {
            val values = country.split("|".toRegex())
            countryKeyCodesMap[values[0]] = values[1]
        }

        fun getForecast(atCity: String, callback: (ArrayList<WeatherItemData>) -> Unit) {
            val url = "https://api.openweathermap.org/data/2.5/forecast"
            val httpBuider = HttpUrl.parse(url)!!.newBuilder()
            httpBuider.addQueryParameter("q", "Montreal,CA")
            httpBuider.addQueryParameter("units", "Celsius")
            httpBuider.addQueryParameter("APPID", "a53cb4b89bd8f8d995a8fcf302627dbe")
            val request = Request.Builder().url(httpBuider.build()).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { }
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
            for (i in 0 until jsonArray.length()) {
                val forecastItem = jsonArray.getJSONObject(i)
                val temperatureItem = forecastItem.getJSONObject("main")
                val temperature = temperatureItem.getDouble("temp").toString()
                forecast.add(WeatherItemData("day", temperature, null))
            }
            return forecast
        }

    }
}