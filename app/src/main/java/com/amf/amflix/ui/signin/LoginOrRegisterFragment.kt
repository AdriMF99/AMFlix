package com.amf.amflix.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R

class LoginOrRegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_or_register, container, false)

        val btnOpenLogin = view.findViewById<Button>(R.id.gotologinButton)
        val btnOpenRegister = view.findViewById<Button>(R.id.gotoregisterButton)

        btnOpenLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_login)
        }

        btnOpenRegister.setOnClickListener {
            findNavController().navigate(R.id.navigation_register)
        }

        return view
    }
}