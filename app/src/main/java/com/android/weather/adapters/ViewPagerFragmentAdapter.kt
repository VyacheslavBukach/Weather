package com.android.weather.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.weather.fragments.ForecastFragment
import com.android.weather.fragments.WeatherFragment


class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> WeatherFragment()
        else -> ForecastFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}