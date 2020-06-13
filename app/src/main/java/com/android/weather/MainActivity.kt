package com.android.weather

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.weather.adapters.ViewPagerFragmentAdapter
import com.android.weather.databinding.ActivityMainBinding
import com.android.weather.fragments.ForecastFragment
import com.android.weather.fragments.HorizontalFlipTransformation
import com.android.weather.fragments.WeatherFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter =
            ViewPagerFragmentAdapter(this)
        binding.viewPager.setPageTransformer(HorizontalFlipTransformation())

        // attaching tab mediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                when (position) {
                    0 -> tab.text = getString(R.string.weather_now)
                    1 -> tab.text = getString(R.string.weather_next)
                }
            }
        ).attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                var fragment: Fragment?
                for (i in 0 until 2) {
                    when (i) {
                        0 -> {
                            fragment =
                                supportFragmentManager.findFragmentByTag("f0") as WeatherFragment
                            fragment.loadWeatherAndUpdate()
                        }
                        1 -> {
                            fragment = supportFragmentManager.findFragmentByTag("f1")
                            if(fragment == null) return true
                            else {
                                fragment =
                                    supportFragmentManager.findFragmentByTag("f1") as ForecastFragment
                                fragment.loadWeatherAndUpdate()
                            }
                        }
                    }
                }
                return true
            }
            R.id.action_search -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_top, menu)

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            menu!!.findItem(R.id.action_search).actionView as SearchView
        val searchPlate =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Введите город"

        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("frag", newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i("frag", query)
                var fragment: Fragment
                for (i in 0 until 2) {
                    when (i) {
                        0 -> {
                            fragment =
                                supportFragmentManager.findFragmentByTag("f0") as WeatherFragment
                            fragment.loadWeatherAndUpdate(query)
                        }
                        1 -> {
                            fragment =
                                supportFragmentManager.findFragmentByTag("f1") as ForecastFragment
                            fragment.loadWeatherAndUpdate(query)
                        }
                    }
                }
                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)

        return super.onCreateOptionsMenu(menu)
    }
}
