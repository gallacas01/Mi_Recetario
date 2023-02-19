package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.categories

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.RemoveCategoryBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch


class RemoveCategoryFragment : Fragment(R.layout.remove_category){

    private lateinit var binding: RemoveCategoryBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch {
            super.onViewCreated(view, savedInstanceState)

            binding = RemoveCategoryBinding.bind(view)
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Eliminar categoría"

            val spinnerCategories = binding.spinnerCategories
            val currentCategories = BDRepository.getAllCategories()
            spinnerCategories.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, currentCategories)

            binding.btnRemoveCategory.setOnClickListener{

                viewLifecycleOwner.lifecycleScope.launch {
                    //Eliminamos la categoría y volvemos a hacer una petición de las categorías actuales
                    var categoryToRemove = spinnerCategories.selectedItem.toString()
                    BDRepository.removeCategory(categoryToRemove, requireContext())
                    val categoriesUpdated = BDRepository.getAllCategories()
                    spinnerCategories.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, categoriesUpdated)
                }
            }
        }
    }

    private suspend fun rellenarTextViewCategorias() :String{

        val categories = BDRepository.getAllCategories()
        var categorias = ""
        if (categories.size == 0){
            categorias = "No hay ninguna categoría registrada."
        }else{
            for (category in categories){
                categorias += category + ", "
            }
            categorias = categorias.substring(0, categorias.lastIndex-1)
        }
        return categorias + "."
    }

}