package com.amf.amflix.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.amf.amflix.databinding.FragmentLoginBinding
import com.amf.amflix.databinding.FragmentRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var golog: TextView
    private lateinit var txtusername: EditText
    private lateinit var txtpass: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        golog = binding.GoLogin
        txtusername = binding.RegUsername
        txtpass = binding.RegPassword

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
        hideBottomNavigation()
    }

    private fun setup() {
        binding.btnReg.setOnClickListener {
            if (txtusername.text.isNotEmpty() && txtpass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        txtusername.text.toString(),
                        txtpass.text.toString()
                    )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Envía el correo de verificación al usuario
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        // Email de verificación enviado exitosamente
                                        Toast.makeText(
                                            requireContext(),
                                            "Usuario creado! Por favor, verifica tu correo electrónico.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Error al enviar el correo de verificación
                                        Toast.makeText(
                                            requireContext(),
                                            "Error al enviar el correo de verificación.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // Error al crear el usuario
                            showAlert()
                        }
                    }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.VISIBLE
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