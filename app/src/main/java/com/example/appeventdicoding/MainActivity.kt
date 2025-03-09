package com.example.appeventdicoding

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appeventdicoding.databinding.ActivityMainBinding
import com.example.appeventdicoding.ui.favorite.FavoriteViewModel
import com.example.appeventdicoding.ui.viewModelFactory.ViewModelFactory
import com.google.android.material.badge.BadgeDrawable
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_favorite, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val factory:ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel:FavoriteViewModel by viewModels<FavoriteViewModel> {
            factory
        }


        viewModel.getFavoriteEvent().observe(this) {
            val badgetDrawable:BadgeDrawable =  binding.navView.getOrCreateBadge(R.id.navigation_favorite)
            badgetDrawable.backgroundColor = Color.BLUE
            badgetDrawable.badgeTextColor =  Color.YELLOW
            badgetDrawable.number = it.size
            badgetDrawable.setVisible(if(it.size >= 1) true else false)
        }



    }
}