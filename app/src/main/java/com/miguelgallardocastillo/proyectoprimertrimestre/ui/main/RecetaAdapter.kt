package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.ItemListBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta


class RecetaAdapter (val recetaClickedListener: (Receta) -> Unit) : RecyclerView.Adapter<RecetaAdapter.ViewHolder>(){

    var listaRecetas = emptyList<Receta>()
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //Inflamos la vista, es decir, la Receta.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
        val recetaActual = listaRecetas[position]
        holder.bind(recetaActual)
        holder.itemView.setOnClickListener {
            recetaClickedListener(recetaActual)
        }
    }

    override fun getItemCount(): Int {
        return listaRecetas.size
    }

    class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view){

        private val binding = ItemListBinding.bind(view)
        fun bind (receta: Receta){

           binding.tituloReceta.text = receta.label
            if(binding.imagenReceta != null){
                binding.imagenReceta.glide(receta.urlImage)
            }
        }
    }
}

//Método para usar el glide en otras clases
fun ImageView.glide(url: String){
    Glide.with(this).load(url).into(this)

}