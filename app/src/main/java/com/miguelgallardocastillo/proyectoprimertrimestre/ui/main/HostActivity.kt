package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.ActivityMainBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository


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
            R.id.bntFavourites -> {
                findNavController(R.id.fragmentContainerView).navigate(R.id.favouritesFragment)
                true
            }
            R.id.btnRecipesByCategory -> {
                findNavController(R.id.fragmentContainerView).navigate(R.id.recipesByCategoryFragment)
                true
            }
            R.id.btnAddCategory -> {
                findNavController(R.id.fragmentContainerView).navigate(R.id.addCategoryFragment)
                true
            }
            R.id.btnModifyCategory -> {
                findNavController(R.id.fragmentContainerView).navigate(R.id.modifyCategoryFragment)
                true
            }
            R.id.btnRemoveCategory -> {
                findNavController(R.id.fragmentContainerView).navigate(R.id.removeCategoryFragment)
                true
            }
            R.id.btnSignOut ->{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("¿Cerrar sesión?").setPositiveButton("Aceptar", DialogInterface.OnClickListener {
                        dialogInterface, i ->

                            BDRepository.signOut()
                            val loginIntent = Intent(this@HostActivity, LoginFragment::class.java)
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(loginIntent)
                            finish()
                    Toast.makeText(this, "La sesión ha finalizado.", Toast.LENGTH_SHORT).show()

                    } ).setNegativeButton("Cancelar",null)
                builder.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}//Fin del HostActivity