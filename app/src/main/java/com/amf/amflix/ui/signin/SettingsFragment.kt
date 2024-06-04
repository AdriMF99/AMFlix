package com.amf.amflix.ui.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnOpenLogin: ImageView
    private lateinit var btnSignOut: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showBottomNavigation()
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        btnOpenLogin = view.findViewById(R.id.btn_open_login)
        btnSignOut = view.findViewById(R.id.btn_sign_out)

        val user = FirebaseAuth.getInstance().currentUser
        updateUI(user)

        btnOpenLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_loginorregister)
        }

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            updateUI(null)
        }

        return view
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        tvUserName.text = "User Name: $name"
                        tvUserEmail.text = "Email: ${user.email}"
                    } else {
                        tvUserName.text = "User Name: N/A"
                    }
                }
                .addOnFailureListener { exception ->
                    tvUserName.text = "User Name: N/A"
                }
            btnOpenLogin.visibility = View.GONE
            btnSignOut.visibility = View.VISIBLE
        } else {
            tvUserName.text = ""
            tvUserEmail.text = ""
            btnOpenLogin.visibility = View.VISIBLE
            btnSignOut.visibility = View.GONE
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.VISIBLE
    }
}
