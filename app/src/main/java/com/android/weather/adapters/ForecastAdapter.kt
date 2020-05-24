package com.android.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.weather.Day
import com.android.weather.R


class ForecastAdapter(private val data: List<Day>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = data[position]
        holder.date.text = listItem.date
        holder.temp.text = listItem.temprepature
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.list_date)
        val temp = itemView.findViewById<TextView>(R.id.list_temperature)
    }
}