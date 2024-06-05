package com.amf.amflix.ui.Favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MercadoresFragment : Fragment() {
    private val tvseriesviewmodel: com.amf.amflix.ui.series.TvSeriesViewModel by activityViewModels()
    private val movieviewmodel: com.amf.amflix.ui.movies.MovieViewModel by activityViewModels()

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
        favoriteMoviesAdapter = FavoriteMoviesAdapter(emptyList()) { openMovieDetails(it) }
        favoriteSeriesAdapter = FavoriteSeriesAdapter(emptyList()) { openSeriesDetails(it) }
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
                            adult = doc.getBoolean("adult") ?: false,
                            backdrop_path = doc.getString("backdrop_path") ?: "",
                            genres = null,
                            id = doc.getLong("movieId")?.toInt() ?: 0,
                            original_language = doc.getString("original_language") ?: "",
                            original_title = doc.getString("original_title"),
                            original_name = doc.getString("original_name"),
                            name = doc.getString("name"),
                            media_type = doc.getString("media_type"),
                            overview = doc.getString("overview") ?: "",
                            popularity = doc.getDouble("popularity") ?: 0.0,
                            poster_path = doc.getString("posterUrl") ?: "",
                            release_date = doc.getString("release_date") ?: "",
                            title = doc.getString("title") ?: "",
                            video = doc.getBoolean("video") ?: false,
                            vote_average = doc.getDouble("vote_average") ?: 0.0,
                            vote_count = doc.getLong("vote_count")?.toInt() ?: 0,
                            tagline = doc.getString("tagline")
                        )
                    }
                    favoriteMoviesAdapter.updateData(movies)
                    recyclerView.adapter = favoriteMoviesAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e("MARCADORES", "Error loading movies: ${exception.message}")
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
                        TVSeries(
                            backdrop_path = doc.getString("backdrop_path") ?: "",
                            first_air_date = doc.getString("first_air_date") ?: "",
                            genres = null,
                            id = doc.getLong("id")?.toInt() ?: 0,
                            media_type = doc.getString("media_type"),
                            name = doc.getString("name") ?: "",
                            origin_country = doc.get("origin_country") as? List<String> ?: emptyList(),
                            original_language = doc.getString("original_language") ?: "",
                            original_name = doc.getString("original_name") ?: "",
                            overview = doc.getString("overview") ?: "",
                            popularity = doc.getDouble("popularity") ?: 0.0,
                            poster_path = doc.getString("poster_path"),
                            vote_average = doc.getDouble("vote_average") ?: 0.0,
                            vote_count = doc.getLong("vote_count")?.toInt() ?: 0,
                            tagline = doc.getString("tagline")
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

    private fun openSeriesDetails(series: TVSeries) {
        tvseriesviewmodel.selected = series
        findNavController().navigate(R.id.navigation_tvdetails)
    }

    private fun openMovieDetails(movie: Movie) {
        movieviewmodel.selected = movie
        findNavController().navigate(R.id.navigation_details)
    }
}
