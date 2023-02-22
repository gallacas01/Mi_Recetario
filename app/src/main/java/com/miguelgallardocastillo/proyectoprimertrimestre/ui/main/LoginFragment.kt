package com.miguelgallardocastillo.proyectoprimertrimestre.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miguelgallardocastillo.proyectoprimertrimestre.R
import com.miguelgallardocastillo.proyectoprimertrimestre.databinding.LoginBinding

class LoginFragment : AppCompatActivity(R.layout.login){

    private lateinit var binding : LoginBinding
    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

         val editTextEmail = binding.etEmailAdress
         val editTextPassword = binding.etPassword
         val logInButton = binding.logInButton
         val sigUpButton = binding.sigUpButton


        //Cuando el usuario desea registrarse.
        sigUpButton.setOnClickListener {
            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()) {

                if (editTextPassword.text.length < 6){
                    Toast.makeText(this, "La contraseña debe contener, al menos, 6 caracteres.", Toast.LENGTH_SHORT).show()
                }else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString())
                        .addOnCompleteListener {

                            if (it.isSuccessful) {

                                db.collection("users").document(uid).set(
                                    hashMapOf(
                                        "id" to uid,
                                        "email" to editTextEmail.text.toString(),
                                        "password" to editTextPassword.text.toString()
                                    )).addOnFailureListener() {
                                    Toast.makeText(this, "Error al registrar los datos.", Toast.LENGTH_SHORT).show()
                                }

                                Toast.makeText(
                                    this,
                                    "Usuaraio registrado correctamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                    binding.etEmailAdress.text.toString(),
                                    binding.etPassword.text.toString())
                                goHome()
                            } else {
                                showAlert()
                            }
                        }//Fin del addOnCompleteListener.
                }


            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            }

        }// Fin del setOnClickListener.

        //Cuando el usuario ya está registrado y quiere acceder.
        logInButton.setOnClickListener {
            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuario correcto.", Toast.LENGTH_SHORT).show()
                            goHome()
                        } else {
                            Toast.makeText(this, "Datos de inicio de sesión  incorrectos.", Toast.LENGTH_SHORT).show()
                        }
                    }//Fin del addOnCompleteListener.
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar al usuario.")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }//Fin del método showAlert

    private fun goHome(){
        val homeIntent  = Intent(this, HostActivity::class.java)
        startActivity(homeIntent)
    }//Fin de la función goHome

}//Fin de la clase.