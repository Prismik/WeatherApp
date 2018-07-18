package com.example.fb.weatherapp.api

import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class DownloadImageTask(listener: Listener): AsyncTask<String, Void, Bitmap?>() {
    interface Listener {
        fun onImageDownloaded(bitmap: Bitmap)
        fun onImageDownloadError()
    }

    companion object {
        // add cache
    }

    private val listener: Listener

    init {
        this.listener = listener
    }

    override fun doInBackground(vararg urls: String): Bitmap? {
        val url = urls.first()
        var bitmap: Bitmap? = null

        try {
            val inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (malformedUrlException: MalformedURLException) {
            print("Malformed URL Exception")
            listener.onImageDownloadError()
        } catch (ioException: IOException) {
            print("Malformed IO Exception")
            listener.onImageDownloadError()
        }

        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        result?.let {
            listener.onImageDownloaded(result)
        } ?: run {
            listener.onImageDownloadError()
        }
    }
}