package com.example.projektandroid1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.projektandroid1.adapters.KrokiViewPagerAdapter
import com.example.projektandroid1.data.KrokiDao
import com.example.projektandroid1.data.ProjektAndroid1Database
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LicznikKrokowActivity : AppCompatActivity() {

    public lateinit var krokiDao: KrokiDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licznik_krokow)

        this.setTitle("KeepFit - Aktywność")
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val tabLayout = findViewById<TabLayout>(R.id.kroki_tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.kroki_view_pager_2)
        val adapter = KrokiViewPagerAdapter(supportFragmentManager, lifecycle)

        CoroutineScope(Dispatchers.IO).launch {
            krokiDao= ProjektAndroid1Database.get(this@LicznikKrokowActivity.application).getKrokiDao()
        }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}