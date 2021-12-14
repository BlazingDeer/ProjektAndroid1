package com.example.projektandroid1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projektandroid1.kaloriefragments.DodajPosilekFragment
import com.example.projektandroid1.kaloriefragments.StatystykiFragment
import com.example.projektandroid1.kaloriefragments.WszystkiePosilkiFragment
import com.example.projektandroid1.krokifragments.KrokiStatystykiFragment
import com.example.projektandroid1.krokifragments.LicznikFragment

class KrokiViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->{
                LicznikFragment()
            }
            1->{
                KrokiStatystykiFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}