package com.example.projektandroid1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projektandroid1.kaloriefragments.DodajPosilekFragment
import com.example.projektandroid1.kaloriefragments.StatystykiFragment
import com.example.projektandroid1.kaloriefragments.WszystkiePosilkiFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->{
                DodajPosilekFragment()
            }
            1->{
                StatystykiFragment()
            }
            2->{
                WszystkiePosilkiFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}