package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.ActivityMainBinding


class HostActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
                inflater.inflate(R.menu.menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.btnHome -> {

                findNavController(R.id.fragmentContainerView).navigate(R.id.mainFragment)
                true
            }
            R.id.favourites -> {

                findNavController(R.id.fragmentContainerView).navigate(R.id.favouritesFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}//Fin del HostActivity