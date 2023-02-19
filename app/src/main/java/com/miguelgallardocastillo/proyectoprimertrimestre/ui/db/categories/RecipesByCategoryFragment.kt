package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.categories

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.FragmentRecipesByCategoryBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.categories.RecipesByCategoryViewModelFactory
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail.DetailFragment
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.main.RecetaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecipesByCategoryFragment : Fragment(R.layout.fragment_recipes_by_category) {

    private lateinit var binding : FragmentRecipesByCategoryBinding
    private val db =  FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val viewModel : RecipesByCategoryViewModel by viewModels{RecipesByCategoryViewModelFactory(db,uid) }
    private val adapter = RecetaAdapter(){ Receta -> viewModel.navigateTo(Receta) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesByCategoryBinding.bind(view).apply {
            recyclerviewRecetasPorCategoria.adapter = adapter
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Categorías favoritas"

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.progress.visibility = if (state.loading) View.VISIBLE else View.GONE

            if (state.recetas?.size == 0){
                binding.title.setText("No se ha registrado ninguna categoría.")
            }else{
                state.recetas?.let {
                    adapter.listaRecetas = state.recetas
                    adapter.notifyDataSetChanged()
                }

                state.navigateTo?.let {
                    findNavController().navigate(
                        R.id.recipes_by_category_fragment_to_detail_fragment,
                        bundleOf(DetailFragment.EXTRA_RECETA to it),
                    )
                    viewModel.onNavigateDone()
                }
            }


        }

    }//Fin del onViewCreated.

}//Fin de la clase.