package com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta

class DetailViewModel(receta: Receta): ViewModel() {
    private val _receta = MutableLiveData(receta)
    val receta: LiveData<Receta> get() = _receta

}

class DetailViewModelFactory(private val receta: Receta) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(receta) as T
    }
}




