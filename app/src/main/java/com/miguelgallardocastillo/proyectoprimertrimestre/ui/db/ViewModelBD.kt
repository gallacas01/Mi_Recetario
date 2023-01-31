package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelBD (db: FirebaseFirestore, uid: String) : ViewModel() {

    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> get() = _state
    private val db =  db
    private val uid = uid

    //Lo que se declare dentro de este bloque de código se ejecutará en el constructor.
    init {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = _state.value?.copy(loading = true)
            val recetas = getFavouriteRecipes()
            _state.value = _state.value?.copy(loading = false, recetas = recetas)
        }
    }//Fin del método init.

    fun navigateTo(receta: Receta) {
        _state.value = _state.value?.copy(navigateTo = receta)
    }

    fun onNavigateDone(){
        _state.value = _state.value?.copy(navigateTo = null)
    }

    data class UiState(
        val loading: Boolean = false,
        val recetas: List<Receta>? = null,
        val navigateTo: Receta? = null
    )

    //Método que devuelve la lista de recetas favoritas de la base de datos.
    private fun getFavouriteRecipes() : List<Receta>{
        var listaRecetasFavoritas = listOf<Receta>()
        //Recuperamos el mapa de recetas favoritas de la base de datos.
        db.collection("users").document(uid).get().addOnSuccessListener {
            val recetasFavoritas = it.get("recetasFavoritas") as HashMap<String, Receta>
            CoroutineScope(Dispatchers.Main).launch {
                listaRecetasFavoritas = recetasFavoritas.values.toList()
            }
        }
        return listaRecetasFavoritas
    }

}//Fin de la clase.

class ViewModelBDFactory(private val db: FirebaseFirestore, private val uid: String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelBD(db,uid) as T
    }
}