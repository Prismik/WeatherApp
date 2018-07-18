package com.example.fb.weatherapp.api

import android.graphics.Bitmap
import android.media.Image
import java.text.SimpleDateFormat
import java.util.*

class WeatherItemData(utcTime: Long, temperature: String, imageId: String): DownloadImageTask.Listener {
    val day: String
    val temperature: String
    val imageUrl: String

    private var callback: ((Bitmap) -> Unit)? = null

    init {
        val dateFormat = SimpleDateFormat("EEEE HH:mm")
        this.day = dateFormat.format(Date(utcTime * 1000))
        this.temperature = temperature
        this.imageUrl = "https://openweathermap.org/img/w/" + imageId + ".png"
    }

    fun getImage(callback: (Bitmap) -> Unit) {
        val task = DownloadImageTask(this)
        this.callback = callback
        task.execute(this.imageUrl)
    }

    override fun onImageDownloaded(result: Bitmap) {
        callback?.invoke(result)
    }

    override fun onImageDownloadError() {
        print("Error loading image")
    }
}