package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import androidx.lifecycle.*
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta

import com.miguelgallardocastillo.proyectoprimertrimestre.model.RemoteConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app_id: String,app_key:String): ViewModel()  {

    private val app_key = app_key
    private val app_id = app_id
    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> get() = _state

    //Lo que se declare dentro de este bloque de código se ejecutará en el constructor.
    init {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = _state.value?.copy(loading = true)
            val recetas = obtenerRecetasRandom()
            _state.value = _state.value?.copy(loading = false, recetas = recetas)
        }
    }//Fin del método init.

    suspend fun obtenerRecetasRandom() : List<Receta> {
        val recipes = RemoteConnection.service.RandomRecetas(app_id,app_key).hits
        val recetas = recipes.map { //El result es la lista que devuelve el RemoteConnection.
            Receta(
                it.recipe.label,
                it.recipe.image,
                it.recipe.calories.toString(),
                it.recipe.url,
                it.recipe.totalNutrients.FAT.quantity.toString(),
                it.recipe.totalNutrients.chocdf.quantity.toString(),
                it.recipe.totalNutrients.PROCNT.quantity.toString(),
                it.recipe.totalWeight.toString()
            )
        }
        return recetas
    }//Fin del método.

    fun navigateTo(receta: Receta) {
        _state.value = _state.value?.copy(navigateTo = receta)
    }

    fun onNavigateDone(){
        _state.value = _state.value?.copy(navigateTo = null)
    }

    //Función que obtiene los resultados obtenidos tras un valor introducido por el usuario.
    suspend fun getCustomResults (busqueda: String) : List<Receta>{
        val recipes = RemoteConnection.service.CustomRecetas(busqueda,app_id,app_key).hits
        val recetas = recipes.map { //El result es la lista que devuelve el RemoteConnection.
            Receta(
                it.recipe.label,
                it.recipe.image,
                it.recipe.calories.toString(),
                it.recipe.url,
                it.recipe.totalNutrients.FAT.quantity.toString(),
                it.recipe.totalNutrients.chocdf.quantity.toString(),
                it.recipe.totalNutrients.PROCNT.quantity.toString(),
                it.recipe.totalWeight.toString()
            )
        }
        return recetas
    }

    data class UiState(
        val loading: Boolean = false,
        val recetas: List<Receta>? = null,
        val navigateTo: Receta? = null
    )

 }//Fin de la clase.


class MainViewModelFactory(private val app_id: String, private val app_key: String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app_id,app_key) as T
    }
}