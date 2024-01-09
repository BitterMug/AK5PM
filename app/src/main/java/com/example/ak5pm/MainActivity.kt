package com.example.ak5pm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.ak5pm.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(CurrentFragment())
        val bottomNav: BottomNavigationView = binding.bottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.current -> {
                    loadFragment(CurrentFragment())
                    true
                }
                R.id.about -> {
                    loadFragment(AboutFragment())
                    true
                }
                else -> {false}
            }
        }

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}