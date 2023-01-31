package com.miguelgallardocastillo.proyectoprimertrimestre.ui.db

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.AddRecipeBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta

class AddRecipeFragment : Fragment(R.layout.add_recipe){

    private lateinit var binding : AddRecipeBinding
    private val db =  FirebaseFirestore.getInstance()

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddRecipeBinding.bind(view)

        binding.btnEnviar.setOnClickListener{

            //Recuperamos el id del usuario que está accediendo a la aplicación.
            val id = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val nombre = binding.editTextRecipeName.text.toString()
            val urlImagen = binding.editTextUrlImage.text.toString()
            val calorias = binding.editTextKcal.text.toString()
            val urlReceta = binding.editTextUrlRecipe.text.toString()
            val grasas = binding.editTextFat.text.toString()
            val carbs =  binding.editTextCarbs.text.toString()
            val proteinas = binding.editTextProtein.text.toString()
            val peso =  binding.editTextWeight.text.toString()
            val receta = Receta(nombre, urlImagen, calorias,urlReceta,grasas,carbs,proteinas,peso)

            //Obtenemos los campos de la colección de usuario para añadir la rececta.
            db.collection("users").document(id).get().addOnSuccessListener {

               val recetasIntroducidas = it.get("recetasIntroducidas") as HashMap<String,Receta>
                recetasIntroducidas.put(nombre, receta)

                db.collection("users").document(id).update("recetasIntroducidas", recetasIntroducidas)
                //Si ha habido éxito, se refrescarán los campos (los seteamos a "").
                binding.editTextRecipeName.setText(" ")
                binding.editTextUrlImage.setText(" ")
                binding.editTextKcal.setText(" ")
                binding.editTextUrlRecipe.setText(" ")
                binding.editTextFat.setText(" ")
                binding.editTextCarbs.setText(" ")
                binding.editTextProtein.setText(" ")
                binding.editTextRecipeName.setText(" ")
                binding.editTextWeight.setText("")

                //Mostramos un mensaje indicando que la receta se ha introducido con éxito.
                insercionExitosa()

            }.addOnFailureListener{
                insercionErronea()
            }

        }//Fin del setOnClickListener

    }//Fin del onViewCreated

    private fun insercionExitosa(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Éxito")
        builder.setMessage("La receta se ha registrado correctamente.")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun insercionErronea(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Error")
        builder.setMessage("No se ha podido registrar la receta")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

}//Fin del fragment