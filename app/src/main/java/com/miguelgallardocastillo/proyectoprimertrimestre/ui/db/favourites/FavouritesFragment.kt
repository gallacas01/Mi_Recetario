package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.favourites

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.FragmentFavouritesBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail.DetailFragment
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.main.RecetaAdapter
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private lateinit var binding : FragmentFavouritesBinding
    private val db =  FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val viewModelBD : ViewModelBD by viewModels{ ViewModelBDFactory(db,uid) }
    private val adapter = RecetaAdapter(){ Receta -> viewModelBD.navigateTo(Receta) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view).apply {
            recyclerviewRecetasFavoritas.adapter = adapter
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Recetas favoritas"


        viewModelBD.state.observe(viewLifecycleOwner) { state ->
            binding.progress.visibility = if (state.loading) View.VISIBLE else View.GONE

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    state.recetas?.collect() {
                        adapter.listaRecetas = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            state.navigateTo?.let {
                it.esFavorita = true
                findNavController().navigate(
                    R.id.action_favouritesFragment_to_DetailFragment,
                    bundleOf(DetailFragment.EXTRA_RECETA to it),
                )
                viewModelBD.onNavigateDone()
            }
        }

    }//Fin del onViewCreated.

}//Fin de la clase.