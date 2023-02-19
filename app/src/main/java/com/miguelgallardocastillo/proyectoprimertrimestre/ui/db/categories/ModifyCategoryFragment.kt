package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.categories

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.ModifyCategoryBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository
import kotlinx.coroutines.launch


class ModifyCategoryFragment : Fragment(R.layout.modify_category){

    private lateinit var binding: ModifyCategoryBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch {
            super.onViewCreated(view, savedInstanceState)

            binding = ModifyCategoryBinding.bind(view)
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Modificar categoría"

            val spinnerCategories = binding.spinnerCategories
            val currentCategories = BDRepository.getAllCategories()
            spinnerCategories.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, currentCategories)


            binding.btnModifyCategory.setOnClickListener{
                val newValue = binding.categoryNewValue.text.toString()
                if (newValue == ""){
                    Toast.makeText(context, "Por favor, escribe que quieres modificar.",Toast.LENGTH_SHORT).show()
                }else{
                    viewLifecycleOwner.lifecycleScope.launch {
                        var oldCategory = spinnerCategories.selectedItem.toString()
                        BDRepository.modifyCategory(oldCategory , newValue, requireContext())
                        val categoriesUpdated = BDRepository.getAllCategories()
                        spinnerCategories.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, categoriesUpdated)
                        binding.categoryNewValue.setText("")
                    }
                }
            }
        }
    }
}