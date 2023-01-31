package com.miguelgallardocastillo.proyectoprimertrimestre.ui.detail

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.FragmentDetailBinding
import com.miguelgallardocastillo.proyectoprimertrimestre.model.Receta
import com.miguelgallardocastillo.proyectoprimertrimestre.ui.main.glide
import kotlin.math.round


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private  val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(arguments?.getParcelable<Receta>(EXTRA_RECETA)!!)
    }
    private val db =  FirebaseFirestore.getInstance()
    private val id = FirebaseAuth.getInstance().currentUser?.uid.toString()

    companion object {
        //El valor de esta constante es estática.
        const val EXTRA_RECETA = "DetailFragment:Receta"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)

        viewModel.receta.observe(viewLifecycleOwner){ receta ->
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Detalles"

            binding.recipeTitle.text = receta.label
            binding.recipeImage.glide(receta.urlImage)
            binding.kcal.text = "Kcal: " + round(receta.calories.toFloat())
            binding.fat.text = "Grasas: " + round(receta.fat.toFloat()) + " g"
            binding.carbs.text = "Carbohidratos: " + round(receta.carbs.toFloat()) + " g"
            binding.protein.text = "Proteínas: " + round(receta.protein.toFloat()) + " g"
            binding.macronutrientes.text = "Macronutrientes"
            binding.weight.text = "Peso: " + round(receta.weight.toFloat()) + " g"

            binding.buttonEnlaceReceta.setOnClickListener {

                val uri = Uri.parse(receta.urlRecipe)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            binding.btnFavourite.setOnClickListener{

                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("¿Añadir receta a favoritos?").setPositiveButton("Añadir",DialogInterface.OnClickListener{
                        dialogInterface, i ->
                    val nombre = receta.label
                    val urlImagen = receta.urlImage
                    val calorias = receta.calories
                    val urlReceta = receta.urlRecipe
                    val grasas = receta.fat
                    val carbs =  receta.carbs
                    val proteinas = receta.protein
                    val peso =  receta.weight
                    val receta = Receta(nombre, urlImagen, calorias,urlReceta,grasas,carbs,proteinas,peso)

                    //Obtenemos los campos de la colección de usuario para añadir la rececta.
                    db.collection("users").document(id).get().addOnSuccessListener {

                        val recetasIntroducidas = it.get("recetasFavoritas") as HashMap<String,Receta>
                        recetasIntroducidas.put(nombre, receta)
                        //Actualizamos el campo que almacena las recetas favoritas.
                        db.collection("users").document(id).update("recetasFavoritas", recetasIntroducidas)
                        //Mostramos un mensaje indicando que la receta se ha introducido con éxito.
                        insercionExitosa()

                        //Cambiamos el color del botón de favoritos a rojo.
                        binding.btnFavourite.setSupportImageTintList(ColorStateList.valueOf(Color.RED))
                        binding.btnFavourite.background.setTint(Color.WHITE)

                    }.addOnFailureListener{
                        insercionErronea()
                    }

                } ).setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                        dialogInterface, i ->  //No se añade la receta a favoritos.
                } )

                if(binding.btnFavourite.supportImageTintList == ColorStateList.valueOf(Color.RED)){
                    builder.setMessage("¿Eliminar receta de favoritos?").setPositiveButton("Eliminar",DialogInterface.OnClickListener{
                            dialogInterface, i ->  binding.btnFavourite.setSupportImageTintList(ColorStateList.valueOf(Color.WHITE))
                        binding.btnFavourite.background.setTint(resources.getColor(R.color.customVerde2))

                        db.collection("users").document(id).get().addOnSuccessListener {
                            val recetasIntroducidas = it.get("recetasIntroducidas") as HashMap<String,Receta>
                            val recetaAeliminar = recetasIntroducidas.remove(receta.label)
                            db.collection("users").document(id).update("recetasFavoritas", recetasIntroducidas)
                        }

                    }).setNegativeButton("Cancelar",DialogInterface.OnClickListener{
                            dialogInterface, i ->  //No se elimina la receta de favoritos.
                    })

                }//Fin del else-if.

                builder.create().show()
            }//Fin del onClickListener.

        }

    }//Fin del onViewCreated.

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

}//Fin del DetailFragment.