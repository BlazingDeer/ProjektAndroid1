package com.example.projektandroid1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.projektandroid1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class KalorieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalorie)

        val tabLayout=findViewById<TabLayout>(R.id.kalorie_tab_layout)
        val viewPager2=findViewById<ViewPager2>(R.id.kalorie_view_pager_2)
        val adapter=ViewPagerAdapter(supportFragmentManager,lifecycle)

        viewPager2.adapter=adapter
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            when(position)
            {
                0->{
                    tab.text="Dodaj Posiłek"
                }
                1->{
                    tab.text="Statystyki"
                }
                2->{
                    tab.text="Wszystkie Posiłki"
                }
            }
        }.attach()
    }


}