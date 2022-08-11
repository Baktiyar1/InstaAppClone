package com.back4app.kotlin.back4appexample.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.back4app.kotlin.back4appexample.fragments.AddPostFragment
import com.back4app.kotlin.back4appexample.fragments.HomeFragment
import com.back4app.kotlin.back4appexample.fragments.ProfileFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> AddPostFragment()
                2 -> ProfileFragment()
                else -> Fragment()
            }
        }
    }