package com.amf.amflix.ui.movies

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.Cast.CastClient
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    private val movieviewmodel: MovieViewModel by activityViewModels()
    private lateinit var v: View
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var movieTitle: TextView
    private lateinit var originalTitle: TextView
    private lateinit var movieReleaseDate: TextView
    private lateinit var movieRating: TextView
    private lateinit var moviePopularity: TextView
    private lateinit var movieVoteCount: TextView
    private lateinit var movieOverview: TextView
    private lateinit var moviePoster: PhotoView
    private lateinit var backdrop: ImageView
    private lateinit var flecha: ImageButton

    private var isExpanded = false
    private val smallWidth = 100 // dp
    private val smallHeight = 150 // dp
    private val largeWidth = 300 // dp
    private val largeHeight = 450 // dp

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
        castRecyclerView = v.findViewById(R.id.cast_list)
        backdrop = v.findViewById(R.id.backgroundImage)
        castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // OnClickListener para cambiar el tamaño de la imagen
        moviePoster.setOnClickListener {
            toggleSize(moviePoster)
        }
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

        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL + movie.backdrop_path)
            .override(350, 200)
            .placeholder(R.drawable.placeholder_load) // Opcional: imagen de carga
            .error(R.drawable.error_image) // Opcional: imagen de error si la carga falla
            .into(backdrop)

        loadCast(movie.id)
    }

    private fun loadCast(movieId: Int) {
        val call = CastClient.getInstance().getMovieCast(movieId)
        call.enqueue(object : retrofit2.Callback<CastResponse> {
            override fun onResponse(call: retrofit2.Call<CastResponse>, response: retrofit2.Response<CastResponse>) {
                if (response.isSuccessful) {
                    val castList = response.body()?.cast ?: emptyList()
                    castAdapter = CastAdapter(castList)
                    castRecyclerView.adapter = castAdapter
                } else {
                    Log.e("DetailFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<CastResponse>, t: Throwable) {
                Log.e("DetailFragment", "Error: ${t.message}")
            }
        })
    }


    private fun toggleSize(view: PhotoView) {
        val startWidth = if (isExpanded) largeWidth else smallWidth
        val endWidth = if (isExpanded) smallWidth else largeWidth
        val startHeight = if (isExpanded) largeHeight else smallHeight
        val endHeight = if (isExpanded) smallHeight else largeHeight

        val widthAnimator = ValueAnimator.ofInt(startWidth, endWidth)
        widthAnimator.addUpdateListener {
            val layoutParams = view.layoutParams
            layoutParams.width = (it.animatedValue as Int).dpToPx()
            view.layoutParams = layoutParams
        }

        val heightAnimator = ValueAnimator.ofInt(startHeight, endHeight)
        heightAnimator.addUpdateListener {
            val layoutParams = view.layoutParams
            layoutParams.height = (it.animatedValue as Int).dpToPx()
            view.layoutParams = layoutParams
        }

        widthAnimator.duration = 300
        heightAnimator.duration = 300

        widthAnimator.start()
        heightAnimator.start()

        isExpanded = !isExpanded
    }

    // Extension function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
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
