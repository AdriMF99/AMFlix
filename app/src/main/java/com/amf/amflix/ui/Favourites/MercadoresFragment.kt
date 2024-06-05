package com.amf.amflix.ui.Favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.movies.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MercadoresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter
    private lateinit var favoriteSeriesAdapter: FavoriteSeriesAdapter
    private lateinit var radioGroup: RadioGroup
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mercadores, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        radioGroup = view.findViewById(R.id.radioGroup)

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        favoriteMoviesAdapter = FavoriteMoviesAdapter(emptyList())
        favoriteSeriesAdapter = FavoriteSeriesAdapter(emptyList())
        recyclerView.adapter = favoriteMoviesAdapter

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonFavMovies -> loadFavoriteMovies()
                R.id.radioButtonFavSeries -> loadFavoriteSeries()
            }
        }

        return view
    }

    private fun loadFavoriteMovies() {
        if (userId != null) {
            db.collection("users").document(userId).collection("favourites")
                .get()
                .addOnSuccessListener { documents ->
                    val movies = documents.map { doc ->
                        Movie(
                            title = doc.getString("title") ?: "",
                            poster_path = doc.getString("posterUrl") ?: ""
                        )
                    }
                    favoriteMoviesAdapter.updateData(movies)
                    recyclerView.adapter = favoriteMoviesAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e("MARCADORES", "Error loading series: ${exception.message}")
                }
        } else {
            Log.e("MARCADORES", "User ID is null")
        }
    }

    private fun loadFavoriteSeries() {
        if (userId != null) {
            db.collection("users").document(userId).collection("tv_favorites")
                .get()
                .addOnSuccessListener { documents ->
                    val series = documents.map { doc ->
                        Serie(
                            name = doc.getString("title") ?: "",
                            poster_path = doc.getString("posterUrl") ?: ""
                        )
                    }
                    favoriteSeriesAdapter.updateData(series)
                    recyclerView.adapter = favoriteSeriesAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e("MARCADORES", "Error loading series: ${exception.message}")
                }
        } else {
            Log.e("MARCADORES", "User ID is null")
        }
    }
}

data class Movie(
    val title: String = "",
    val poster_path: String = ""
)

data class Serie(
    val name: String = "",
    val poster_path: String = ""
)