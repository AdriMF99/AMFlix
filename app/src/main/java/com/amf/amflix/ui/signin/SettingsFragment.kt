package com.amf.amflix.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val btnOpenLogin = view.findViewById<ImageView>(R.id.btn_open_login)
        val btnSignOut = view.findViewById<ImageView>(R.id.btn_sign_out)

        btnOpenLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_loginorregister)
        }

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Sesión cerrada!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}