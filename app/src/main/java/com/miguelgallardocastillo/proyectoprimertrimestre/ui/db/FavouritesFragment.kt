package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.FragmentFavouritesBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail.DetailFragment
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.main.RecetaAdapter


class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private lateinit var binding : FragmentFavouritesBinding
    private val db =  FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val viewModelBD : ViewModelBD by viewModels{ViewModelBDFactory(db,uid) }
    private val adapter = RecetaAdapter(){ Receta -> viewModelBD.navigateTo(Receta) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view).apply {
            recyclerviewRecetasFavoritas.adapter = adapter
        }

        viewModelBD.state.observe(viewLifecycleOwner) { state ->
            binding.progress.visibility = if (state.loading) View.VISIBLE else View.GONE
            state.recetas?.let {
                adapter.listaRecetas = it
                adapter.notifyDataSetChanged()
            }

            state.navigateTo?.let {
                findNavController().navigate(
                    R.id.action_favouritesFragment_to_DetailFragment,
                    bundleOf(DetailFragment.EXTRA_RECETA to it),
                )
                viewModelBD.onNavigateDone()
            }
        }

    }//Fin del onViewCreated.

}//Fin de la clase.