package com.example.ontheroad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ontheroad.ui.home.HomeFragment
import com.example.ontheroad.ui.details.DetailsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // Maken van de functie voor het selecteren van de fragments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_details -> selectedFragment = DetailsFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
            }
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    // Data class voor de autodetails
    data class AutoInfo(
        val kenteken: String,
        val voertuigsoort: String,
        val merk: String,
        val handelsbenaming: String,
        val eerste_kleur: String,
        val taxi_indicator: String,
        val wam_verzekerd: String
    ) : java.io.Serializable
}
