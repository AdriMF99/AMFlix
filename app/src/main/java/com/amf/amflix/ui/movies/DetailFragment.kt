package com.amf.amflix.ui.movies

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.Cast.CastClient
import com.amf.amflix.retrofit.Crew.CrewClient
import com.amf.amflix.retrofit.Video.VideoClient
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.amf.amflix.retrofit.models.Crew.CrewResponse
import com.amf.amflix.retrofit.models.Video.Video
import com.amf.amflix.retrofit.models.Video.VideoResponse
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.movies.MovieClient
import com.amf.amflix.ui.Video.VideoDialogFragment
import com.amf.amflix.ui.Video.VideoPlayerDialogFragment
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.qamar.curvedbottomnaviagtion.setMargins
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {
    private val movieviewmodel: MovieViewModel by activityViewModels()
    private lateinit var v: View
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var crewRecyclerView: RecyclerView
    private lateinit var crewAdapter: CrewAdapter
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
    private lateinit var currentMovieVideos: List<Video>
    private lateinit var playTrailerButton: FloatingActionButton
    private lateinit var genresContainer: LinearLayout
    private lateinit var tag: TextView
    private lateinit var ratingBar: RatingBar

    var movieClient: MovieClient?= null


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
        initializeCurrentMovieVideos(movieviewmodel.selected?.id ?: return)
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
        crewRecyclerView = v.findViewById(R.id.crew_list)
        backdrop = v.findViewById(R.id.backgroundImage)
        castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        crewRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        playTrailerButton = v.findViewById(R.id.playTrailerButton)
        genresContainer = v.findViewById(R.id.genresContainer)
        tag = v.findViewById(R.id.taglineMovie)
        movieClient = MovieClient.instance
        ratingBar = v.findViewById(R.id.ratingBar)

        // OnClickListener para cambiar el tamaño de la imagen
        moviePoster.setOnClickListener {
            toggleSize(moviePoster)
        }

        playTrailerButton.setOnClickListener {
            if (::currentMovieVideos.isInitialized && currentMovieVideos.isNotEmpty()) {
                showVideoOptionsDialog()
            } else {
                Toast.makeText(requireContext(), "No videos available :(", Toast.LENGTH_SHORT).show()
            }
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
        val movieRating = movie.vote_average / 2.0f
        ratingBar.rating = movieRating.toFloat()

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

        // Llamar al servicio para obtener los detalles de la película
        movieClient?.getMovieDetails(movie.id)?.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val movieDetails = response.body()
                    movieDetails?.genres?.let { genres ->
                        // Remueve todos los géneros previamente mostrados
                        genresContainer.removeAllViews()

                        // Configura el margen para el contenedor principal
                        genresContainer.setPadding(0, 8, 0, 8)

                        // Itera sobre cada género y crea una vista TextView para mostrarlo
                        genres.forEach { genre ->
                            val genreTextView = TextView(context).apply {
                                text = genre.name
                                setTextColor(ContextCompat.getColor(context, R.color.white))
                                setBackgroundResource(R.drawable.genre_item_background)
                                setPadding(16, 8, 16, 8)
                                setTextSize(14f)

                                // Configura el margen para cada elemento individual
                                val layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(8, 0, 8, 0)
                                }
                                layoutParams.setMargins(12, 0, 12, 0)
                                layoutParams.gravity = Gravity.CENTER
                                this.layoutParams = layoutParams
                            }

                            // Agrega la vista TextView al contenedor principal
                            genresContainer.addView(genreTextView)
                        }
                    }
                    movieDetails?.tagline?.let { tagline ->
                        tag.text = tagline
                    }
                } else {
                    Log.e("DetailFragment", "Error al obtener los detalles de la película: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.e("DetailFragment", "Error al obtener los detalles de la película: ${t.message}")
            }
        })


        loadCast(movie.id)
        loadCrew(movie.id)
        initializeCurrentMovieVideos(movie.id)
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

    private fun loadCrew(movieId: Int) {
        val call = CrewClient.getInstance().getMovieCrew(movieId)
        call.enqueue(object : retrofit2.Callback<CrewResponse> {
            override fun onResponse(call: retrofit2.Call<CrewResponse>, response: retrofit2.Response<CrewResponse>) {
                if (response.isSuccessful) {
                    val crewList = response.body()?.crew ?: emptyList()
                    crewAdapter = CrewAdapter(crewList)
                    crewRecyclerView.adapter = crewAdapter
                } else {
                    Log.e("DetailFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<CrewResponse>, t: Throwable) {
                Log.e("DetailFragment", "Error: ${t.message}")
            }
        })
    }

    private fun initializeCurrentMovieVideos(movieId: Int) {
        lifecycleScope.launch {
            try {
                val call = VideoClient.getInstance().videoService.getMovieVideos(movieId, Constants.API_KEY)
                call.enqueue(object : Callback<VideoResponse> {
                    override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                        if (response.isSuccessful) {
                            currentMovieVideos = response.body()?.results?.filter { it.site == "YouTube" } ?: emptyList()
                            Log.d("DetailFragment", "Videos: $currentMovieVideos") // Agrega esto para depurar
                        } else {
                            Log.e("DetailFragment", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                        Log.e("DetailFragment", "Error: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showVideoOptionsDialog() {
        if (::currentMovieVideos.isInitialized && currentMovieVideos.isNotEmpty()) {
            // Muestra el diálogo de selección de videos
            val dialogFragment = VideoDialogFragment(currentMovieVideos)
            dialogFragment.show(parentFragmentManager, "videoDialog")
        } else {
            Toast.makeText(requireContext(), "No videos available :(", Toast.LENGTH_SHORT).show()
            Log.e("DetailFragment", "No videos available")
        }
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
