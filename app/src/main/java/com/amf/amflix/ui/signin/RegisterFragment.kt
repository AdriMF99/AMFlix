package com.amf.amflix.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.amf.amflix.databinding.FragmentRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var golog: TextView
    private lateinit var txtusername: EditText
    private lateinit var txtpass: EditText
    private lateinit var txtname: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        golog = binding.GoLogin
        txtusername = binding.RegUsername
        txtpass = binding.RegPassword
        txtname = binding.RegName

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.GONE

        golog.setOnClickListener {
            Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.navigation_login)
        }

        setup()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setup() {
        binding.btnReg.setOnClickListener {
            if (txtusername.text.isNotEmpty() && txtpass.text.isNotEmpty() && txtname.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        txtusername.text.toString(),
                        txtpass.text.toString()
                    )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        // Guardar nombre de usuario y foto en Firestore
                                        if (user != null) {
                                            saveUserToFirestore(user.uid, txtname.text.toString())
                                        }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al enviar el correo de verificación.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            showAlert()
                        }
                    }
            }
        }
    }

    private fun saveUserToFirestore(userId: String, name: String) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "userId" to userId,
            "name" to name,
            "email" to FirebaseAuth.getInstance().currentUser?.email,
        )
        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario creado! Por favor, verifica tu correo electrónico.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar el usuario", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Error con el usuario!!")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
