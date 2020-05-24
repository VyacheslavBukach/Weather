package com.android.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.weather.adapters.ViewPagerFragmentAdapter
import com.android.weather.databinding.ActivityMainBinding
import com.android.weather.fragments.HorizontalFlipTransformation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
                when(position) {
                    0 -> tab.text = getString(R.string.weather_now)
                    1 -> tab.text = getString(R.string.weather_next)
                }
            }
        ).attach()
    }

}
