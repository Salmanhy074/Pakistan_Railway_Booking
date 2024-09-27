package com.example.railwaybooking

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Admin_Home : AppCompatActivity() {

    private val homeFragment = AdminFrag_Home()
    private val trainFragment = AdminFrag_Train()
    private val adminProfileFragment = AdminProfileFrag()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.admin_bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.nav_trains -> {
                    loadFragment(trainFragment)
                    true
                }

                R.id.nav_profile -> {
                    loadFragment(adminProfileFragment)
                    true
                }

                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(homeFragment)
            bottomNavigationView.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}