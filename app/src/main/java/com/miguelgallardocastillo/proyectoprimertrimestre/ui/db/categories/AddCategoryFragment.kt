package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.AddCategoryBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository
import kotlinx.coroutines.launch


class AddCategoryFragment : Fragment(R.layout.add_category) {

    private lateinit var binding : AddCategoryBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = AddCategoryBinding.bind(view)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Registar categoría"

        binding.btnEnviarCategoria.setOnClickListener{
            val categoriIntroducida = binding.categoryInput.text.toString().trim()
            if (categoriIntroducida != ""){
                viewLifecycleOwner.lifecycleScope.launch {
                    val res = BDRepository.addCategory(categoriIntroducida, requireContext())
                }
                binding.categoryInput.setText("")
            }else{
                Toast.makeText(context, "Por favor, introduce alguna comida favorita.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}//Fin del fragment.