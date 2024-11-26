package com.example.skegoapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.skegoapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cek apakah activity ini sudah dimulai
        Log.d("MainActivity", "MainActivity started successfully.")

        // Ambil reference dari BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)

        // Ambil reference dari NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main2) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up BottomNavigationView untuk bekerja dengan NavController
        bottomNavigationView.setupWithNavController(navController)
    }
}
