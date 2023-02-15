package com.example.forecast.ui.weather.future.list

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.forecast.R
import com.example.forecast.data.db.specificFutureWeatherEntries.SimpleFutureWeatherEntries
import com.example.forecast.data.network.ICONURL
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


class FutureWeatherItem(val weatherEntry: SimpleFutureWeatherEntries):Item<GroupieViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            val weatherCondition = viewHolder.itemView.findViewById<TextView>(R.id.condition)
            val tempMax = viewHolder.itemView.findViewById<TextView>(R.id.av_temp)
            val date = viewHolder.itemView.findViewById<TextView>(R.id.weather_date)
            val conditionIcon = viewHolder.itemView.findViewById<ImageView>(R.id.condition_icon)
            weatherCondition.text = weatherEntry.conditionText
            tempMax.text = weatherEntry.avTemperature.toString()
            date.text = updateDate()
            Glide.with(this.itemView).load("$ICONURL${weatherEntry.conditionIcon}.png").into(conditionIcon)
        }
    }

    override fun getLayout(): Int = R.layout.future_weather_item

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate():String{
        val dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        return weatherEntry.datetime.format(dateFormat)
    }
}