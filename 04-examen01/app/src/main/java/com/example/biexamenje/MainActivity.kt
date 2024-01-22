package com.example.biexamenje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.biexamenje.fragments.ArtistasFragment
import com.example.biexamenje.fragments.HomeFragment
import com.example.biexamenje.fragments.ObrasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val artistasFragment = ArtistasFragment()
        val obrasFragment = ObrasFragment()

        makeCurrentFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home->makeCurrentFragment(homeFragment)
                R.id.artistas->makeCurrentFragment(artistasFragment)
                R.id. obras->makeCurrentFragment(obrasFragment)
            }
            true
        }
    }


    private fun makeCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
}