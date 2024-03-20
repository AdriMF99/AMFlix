package com.amf.amflix.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    private val movieviewmodel: MovieViewModel by activityViewModels()
    private lateinit var v: View
    private lateinit var movieTitle: TextView
    private lateinit var originalTitle: TextView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieRating: TextView
    private lateinit var moviePopularity: TextView
    private lateinit var movieVoteCount: TextView
    private lateinit var movieOverview: TextView
    private lateinit var moviePoster: PhotoView
    private lateinit var flecha: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_detail, container, false)
        initializeViews()

        hideBottomNavigation()

        flecha.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }



        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    private fun initializeViews() {
        movieTitle = v.findViewById(R.id.movieTitle)
        originalTitle = v.findViewById(R.id.originalTitle)
        movieReleaseDate = v.findViewById(R.id.movieReleaseDate)
        movieRating = v.findViewById(R.id.movieRating)
        moviePopularity = v.findViewById(R.id.moviePopularity)
        movieVoteCount = v.findViewById(R.id.movieVoteCount)
        movieOverview = v.findViewById(R.id.movieOverview)
        moviePoster = v.findViewById(R.id.movieThumbnail)
        flecha = v.findViewById(R.id.backarrow)
    }

    private fun updateUI() {
        val movie = movieviewmodel.selected ?: return

        movieTitle.text = movie.title
        originalTitle.text = movie.original_title
        movieReleaseDate.text = movie.release_date
        movieRating.text = movie.vote_average.toString()
        moviePopularity.text = movie.popularity.toString()
        movieVoteCount.text = movie.vote_count.toString()
        movieOverview.text = movie.overview

        // Carga la imagen de la portada utilizando Glide
        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL + movie.poster_path)
            .override(600, 900)
            .placeholder(R.drawable.placeholder_load) // Opcional: imagen de carga
            .error(R.drawable.error_image) // Opcional: imagen de error si la carga falla
            .into(moviePoster)
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
