package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.FragmentMainBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail.DetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main), SearchView.OnQueryTextListener {

    private val viewModel:MainViewModel by viewModels{MainViewModelFactory(getString(R.string.app_id),getString(R.string.app_key))}
    private val adapter = RecetaAdapter(){ Receta -> viewModel.navigateTo(Receta) }
    private lateinit var binding: FragmentMainBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view).apply {
            recyclerviewRecetas.adapter = adapter
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
            getString(R.string.app_name)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.textoBienvenida.text = "¿Qué te apetece preparar hoy?"
            binding.progress.visibility = if (state.loading) VISIBLE else GONE
            state.recetas?.let {
                adapter.listaRecetas = it
                adapter.notifyDataSetChanged()
            }

            state.navigateTo?.let {
                findNavController().navigate(
                    R.id.action_mainFragment_to_detailFragment,
                    bundleOf(DetailFragment.EXTRA_RECETA to it),
                )
                viewModel.onNavigateDone()
            }
        }

        //Actualizar el adapter cuando se refresque el recycler  view.
        binding.swipe.setOnRefreshListener {

            CoroutineScope(Dispatchers.Main).launch {
                binding.progress.visibility = VISIBLE
                binding.textoBienvenida.text = "¡Disfruta de más recetas!"
                val recipes = viewModel.obtenerRecetasRandom()
                val recetas =
                    recipes.map { //El result es la lista que devuelve el RemoteConnection.
                        Receta(
                            it.label,
                            it.image,
                            it.calories.toString(),
                            it.mealType.toString(),
                            it.urlReceta,
                            it.fat,
                            it.carbs,
                            it.protein,
                            it.weight
                        )
                    }
                adapter.listaRecetas = recetas
                adapter.notifyDataSetChanged()
                binding.progress.visibility = GONE
            }
            binding.swipe.isRefreshing = false
        }
        binding.searchView.setOnQueryTextListener(this)
    }//Fin del onViewCreated

    //Método al que se llama cuando se pulsa en el botón de buscar en el searchView
    override fun onQueryTextSubmit(busqueda: String?): Boolean {

        if (!busqueda.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.progress.visibility = VISIBLE

                adapter.listaRecetas = viewModel.getCustomResults(busqueda)
                adapter.notifyDataSetChanged()
                binding.progress.visibility = GONE
            }
        }
        return true
    }

    override fun onQueryTextChange(búsqueda: String?): Boolean {
        return true
    }

}//Fin del fragment.


