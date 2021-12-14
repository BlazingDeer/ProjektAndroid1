package com.example.projektandroid1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.projektandroid1.adapters.KrokiViewPagerAdapter
import com.example.projektandroid1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LicznikKrokowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licznik_krokow)

        val tabLayout = findViewById<TabLayout>(R.id.kroki_tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.kroki_view_pager_2)
        val adapter = KrokiViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Licznik Kroków"
                }
                1 -> {
                    tab.text = "Statystyki"
                }
            }
        }.attach()
    }
}