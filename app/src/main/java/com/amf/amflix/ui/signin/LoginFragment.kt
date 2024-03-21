package com.amf.amflix.ui.signin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.amf.amflix.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var goreg: TextView
    private lateinit var txtusername: EditText
    private lateinit var txtpass: EditText
    private lateinit var logoogle: LinearLayout
    private val GOOGLE_SIGN_IN = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigation()
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val view = binding.root
        goreg = binding.GoRegister
        txtusername = binding.LogUsername
        txtpass = binding.LogPassword

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.GONE

        goreg.setOnClickListener {
            Toast.makeText(requireContext(), "Register", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.navigation_register)
        }

        setup()

        return view
    }

    private fun setup() {
        binding.btnLogin.setOnClickListener {
            if (txtusername.text.isNotEmpty() && txtpass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        txtusername.text.toString(),
                        txtpass.text.toString()
                    )
                    .addOnCompleteListener { loginTask ->
                        if (loginTask.isSuccessful) {
                            // Verificar si el usuario ha verificado su correo electrónico
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user != null && user.isEmailVerified) {
                                // El usuario ha verificado su correo electrónico, permitir el inicio de sesión
                                Toast.makeText(
                                    requireContext(),
                                    "Has iniciado sesión!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // El usuario no ha verificado su correo electrónico
                                Toast.makeText(
                                    requireContext(),
                                    "Por favor, verifica tu correo electrónico primero.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Error al iniciar sesión
                            showAlert()
                        }
                    }
            }
        }

        binding.LogGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(requireContext(), "Has iniciado sesión!", Toast.LENGTH_SHORT).show()
                        } else{
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert()
            }

        }
    }
}