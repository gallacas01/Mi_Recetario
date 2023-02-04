package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.favourites

import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.db.BDRepository
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
            val recetas = BDRepository.getFavouriteRecipes()
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

}//Fin de la clase.

class ViewModelBDFactory(private val db: FirebaseFirestore, private val uid: String): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelBD(db,uid) as T
    }
}