package com.amf.amflix.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.amf.amflix.databinding.FragmentDashboardBinding
import com.amf.amflix.ui.movies.MovieListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var txtUserLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        val view = binding.root

        txtUserLog = binding.txtUserLog

        val cardView1 = binding.card1
        val cardView2 = binding.card2
        val cardView3 = binding.card3
        val cardView4 = binding.card4
        val cardView5 = binding.card5
        val cardView6 = binding.card6

        cardView1.setOnClickListener {
            Toast.makeText(requireContext(), "Populares", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.navigation_movies)
        }

        cardView2.setOnClickListener {
            Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.navigation_login)
        }

        cardView3.setOnClickListener {
                Toast.makeText(requireContext(), "Home", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.navigation_home)
        }

        binding.txtUserLog.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Sesión cerrada!", Toast.LENGTH_SHORT).show()
        }


        return view
    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}
