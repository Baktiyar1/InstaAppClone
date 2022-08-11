package com.back4app.kotlin.back4appexample.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.back4app.kotlin.back4appexample.R
import com.back4app.kotlin.back4appexample.adapters.ViewPagerAdapter
import com.back4app.kotlin.back4appexample.databinding.ActivityMainBinding
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.apply {
            viewPager.adapter = adapter
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.homeFragment -> viewPager.currentItem = 0
                    R.id.addPostFragment -> viewPager.currentItem = 1
                    R.id.profileFragment -> viewPager.currentItem = 2
                }
                return@setOnItemSelectedListener true
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> bottomNavigationView.selectedItemId = R.id.homeFragment
                        1 -> bottomNavigationView.selectedItemId = R.id.addPostFragment
                        2 -> bottomNavigationView.selectedItemId = R.id.profileFragment
                    }
                    super.onPageSelected(position)
                }
            })

            logoutBtn.setOnClickListener {
                ParseUser.logOut()
                finishAffinity()
                startActivity(Intent(this@MainActivity, LogInActivity::class.java))
            }

            val parseUser = ParseUser.getCurrentUser()

            toolbarPersonTv.text = parseUser.username
        }
    }
}