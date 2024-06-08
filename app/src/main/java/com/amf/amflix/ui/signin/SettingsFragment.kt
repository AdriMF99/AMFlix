package com.amf.amflix.ui.signin

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnOpenLogin: ImageView
    private lateinit var btnSignOut: ImageView
    private lateinit var userAvatar: ImageView
    private lateinit var switchDarkMode: Switch

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
        userAvatar = view.findViewById(R.id.user_avatar)
        switchDarkMode = view.findViewById(R.id.switch_dark_mode)

        val user = FirebaseAuth.getInstance().currentUser
        updateUI(user)

        switchDarkMode.isChecked = isDarkModeOn()
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            restartActivity()
        }

        btnOpenLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_loginorregister)
        }

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            userAvatar.setImageResource(R.drawable.user_avatar)
            Toast.makeText(requireContext(), "You have logged out!", Toast.LENGTH_SHORT).show()
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
                        val profileImageResource = document.getLong("profileImageResource")?.toInt()
                        tvUserName.text = "$name"
                        tvUserEmail.text = "${user.email}"
                        if (profileImageResource != null) {
                            userAvatar.setImageResource(profileImageResource)
                        } else {
                            userAvatar.setImageResource(R.drawable.user_avatar)
                        }
                    } else {
                        tvUserName.text = "User Name: N/A"
                    }
                }
                .addOnFailureListener { exception ->
                    //tvUserName.text = "User Name: N/A"
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

    private fun isDarkModeOn(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }
}
