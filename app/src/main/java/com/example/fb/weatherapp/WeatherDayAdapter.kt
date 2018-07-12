package com.example.fb.weatherapp

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fb.weatherapp.api.WeatherItemData
import kotlinx.android.synthetic.main.weather_recycler_list_item.view.*

class WeatherDayAdapter: RecyclerView.Adapter<WeatherDayAdapter.ViewHolder>() {
    private var data: List<WeatherItemData> = ArrayList<WeatherItemData>()
    fun configure(data: List<WeatherItemData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_recycler_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.configure(data.get(index))
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var view: View = view

        init {
            view.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun configure(data: WeatherItemData) {
            view.dayLabel.text = data.day
            view.temperatureLabel.text = data.temperature
        }
    }
}