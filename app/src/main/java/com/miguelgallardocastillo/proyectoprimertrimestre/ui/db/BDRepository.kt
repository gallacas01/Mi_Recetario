package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.model.RemoteConnection
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random


object BDRepository {

    val db =  FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val app_id = "26dc4baa"
    val app_key = "5c47df10936efed9ecfa97eb1e652ac8"

    //Método que devuelve la lista de recetas favoritas de la base de datos.
    fun getFavouriteRecipesFlow() : Flow<List<Receta>>{
        return db.collection("users").document(uid).collection("recetasFavoritas").snapshots().map {
                snapshot -> snapshot.toObjects(Receta::class.java)
        }
    }

    fun deleteRecipeFromFavourites (receta: Receta, toast: Toast){
        db.collection("users").document(uid).collection("recetasFavoritas").whereEqualTo("label",receta.label).get().addOnCompleteListener {
            if (it.isSuccessful){
                val idReceta = it.result.first().id
                db.collection("users").document(uid).collection("recetasFavoritas").document(idReceta).delete().addOnCompleteListener{
                    if (it.isSuccessful){
                        toast.show()
                    }
                }
            }
        }
    }

    //Método que inserta una categoría en la lista de categorías de comida.
    suspend fun addCategory(categoria:String, context: Context){

        //Primero comprobamos que la categoría no exista ya en la base de datos.
        val snapshot = db.collection("users").document(uid).collection("categorias").whereEqualTo("categoria",categoria).get().await()
        if (snapshot.isEmpty) {
            db.collection("users").document(uid).collection("categorias").add(hashMapOf("categoria" to categoria)).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(context,"La categoría se ha registrado correctamente.", Toast.LENGTH_SHORT ).show()
                }
            }
        }else{
            Toast.makeText(context,"Error: La categoría ya existe.", Toast.LENGTH_SHORT ).show()
        }
    }

    suspend fun getAllCategories () : List<String> {
        val coleccionDeCategorias = db.collection("users").document(uid).collection("categorias").get().await()
        val listaDeCategorias = mutableListOf<String>()
        if (!coleccionDeCategorias.isEmpty){
            for (categoriaBD in coleccionDeCategorias){
                listaDeCategorias.add(categoriaBD.get("categoria").toString())
            }
        }
        return listaDeCategorias
    }

    suspend fun getCustomRecipesByCategory () : List<Receta>{
        if (getAllCategories().size == 0){
            return emptyList<Receta>()
        }else{
            val listaDeCategorias : List<String> = getAllCategories()
            val listaConRecetasDeTodasLasCategorias = mutableListOf<Receta>()
            val listaFinalConRecetas = mutableListOf<Receta>()

            //Recorremos cada categoría de la lista de categorías y obtenemos 20 recetas filtrando por cada categoría.
            for (categoria in listaDeCategorias){
                val recipes = RemoteConnection.service.CustomRecetas(categoria,app_id,app_key).hits
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
                //Añadimos la lista de recetas obtenidas a la lista que contiene todas las recetas.
                listaConRecetasDeTodasLasCategorias.addAll(recetas)
            }
            for (i in 1..20){
                listaFinalConRecetas.add(listaConRecetasDeTodasLasCategorias[Random.nextInt(listaConRecetasDeTodasLasCategorias.size)])
            }
            return listaFinalConRecetas
        }
    }

    suspend fun modifyCategory (oldValue :String , newValue:String, context: Context){
        val snapshot = db.collection("users").document(uid).collection("categorias").whereEqualTo("categoria", oldValue).get().await()
        if (!snapshot.isEmpty){
            val category = snapshot.documents
            var idCategory = category.first().id
            db.collection("users").document(uid).collection("categorias").document(idCategory).update("categoria", newValue).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(context,"La categoría ha sido modificada correctamente.", Toast.LENGTH_SHORT ).show()
                }else{
                    Toast.makeText(context,"Error: La categoría no ha podido ser eliminada.", Toast.LENGTH_SHORT ).show()
                }
            }
        }
    }

    suspend fun removeCategory(categoria: String, context: Context){
         val snapshot = db.collection("users").document(uid).collection("categorias").whereEqualTo("categoria", categoria).get().await()
         if (!snapshot.isEmpty){
             val categoria = snapshot.documents
             var idCategoria = categoria.first().id
             db.collection("users").document(uid).collection("categorias").document(idCategoria).delete().addOnCompleteListener {
                 if (it.isSuccessful){
                     Toast.makeText(context,"La categoría ha sido eliminada correctamente.", Toast.LENGTH_SHORT ).show()
                 }else{
                     Toast.makeText(context,"Error: La categoría no ha podido ser eliminada.", Toast.LENGTH_SHORT ).show()
                 }
             }
         }
     }//Fin del método.

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

}