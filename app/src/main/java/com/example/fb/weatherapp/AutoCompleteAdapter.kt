package com.example.fb.weatherapp

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.fb.weatherapp.api.CityItemData
import com.example.fb.weatherapp.api.WeatherItemData
import kotlinx.android.synthetic.main.city_recycler_list_item.view.*

interface AutoCompleteAdapterDelegate {
    fun didSelectItem(item: CityItemData)
}

class AutoCompleteAdapter: RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder>() {
    var delegate: AutoCompleteAdapterDelegate? = null

    private var data: List<CityItemData> = ArrayList<CityItemData>()
    fun configure(data: List<CityItemData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompleteAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_recycler_list_item, parent, false)
        return AutoCompleteAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AutoCompleteAdapter.ViewHolder, index: Int) {
        holder.configure(data[index])
        holder.delegate = delegate
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        var delegate: AutoCompleteAdapterDelegate? = null
        private lateinit var cityData: CityItemData
        private var view: View = view

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            delegate?.didSelectItem(cityData)
        }

        fun configure(city: CityItemData) {
            cityData = city
            view.city_label.text = city.name
        }
    }
}