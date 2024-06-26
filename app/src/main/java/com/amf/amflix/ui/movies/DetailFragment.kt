package com.amf.amflix.ui.movies

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
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
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.Cast.CastClient
import com.amf.amflix.retrofit.Crew.CrewClient
import com.amf.amflix.retrofit.Images.ImagesClient
import com.amf.amflix.retrofit.Review.ReviewClient
import com.amf.amflix.retrofit.Video.VideoClient
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.amf.amflix.retrofit.models.Crew.CrewResponse
import com.amf.amflix.retrofit.models.Images.ImagesResponse
import com.amf.amflix.retrofit.models.Review.ReviewResponse
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    private lateinit var playTrailerButton: LottieAnimationView
    private lateinit var genresContainer: LinearLayout
    private lateinit var tag: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewsContainer: LinearLayout
    private lateinit var showReviewsButton: LottieAnimationView
    private lateinit var likeButton: LottieAnimationView
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsOverlay: FrameLayout
    private lateinit var postersRecyclerView: RecyclerView
    private var reviewsVisible: Boolean = false

    private var isLiked = false

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
        playTrailerButton.playAnimation()
        showReviewsButton.playAnimation()

        hideBottomNavigation()
        setupOutsideTouchListener()

        flecha.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        setupLikeButton()
        checkIfMovieIsFavorite()
        initializeCurrentMovieVideos(movieviewmodel.selected?.id ?: return)
        showReviewsButton.setOnClickListener {
            toggleReviewsVisibility()
        }

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
        showReviewsButton = v.findViewById(R.id.showReviewsButton)
        likeButton = v.findViewById(R.id.likeButton)
        reviewsContainer = v.findViewById(R.id.reviewsContainer)
        reviewsRecyclerView = v.findViewById(R.id.reviewsRecyclerView)
        reviewsOverlay = v.findViewById(R.id.reviewsOverlay)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postersRecyclerView = v.findViewById(R.id.postersRecyclerView)
        postersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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

        val params = reviewsContainer.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 100.dpToPx()
        reviewsContainer.layoutParams = params
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
            .placeholder(R.drawable.placeholder_load)
            .error(R.drawable.error_image)
            .into(moviePoster)

        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL + movie.backdrop_path)
            .override(350, 200)
            .placeholder(R.drawable.placeholder_load)
            .error(R.drawable.error_image)
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
        fetchReviews(movie.id)
        fetchPosters(movie.id)
        initializeCurrentMovieVideos(movie.id)
    }

    private fun checkIfMovieIsFavorite() {
        val user = auth.currentUser
        val movie = movieviewmodel.selected ?: return

        if (user != null) {
            val userId = user.uid
            val docRef = db.collection("users").document(userId)
                .collection("favourites").document(movie.id.toString())

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    isLiked = true
                    likeButton.progress = 1.0f
                } else {
                    isLiked = false
                    likeButton.progress = 0.0f
                }
            }.addOnFailureListener { e ->
                Log.w("DetailFragment", "Error checking favorites", e)
            }
        }
    }

    private fun setupLikeButton() {
        likeButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                val userId = user.uid
                val movie = movieviewmodel.selected ?: return@setOnClickListener

                if (isLiked) {
                    removeFavorite(userId, movie.id)
                } else {
                    addFavorite(userId, movie)
                }
                isLiked = !isLiked
                toggleLikeButtonAnimation()
            } else {
                Toast.makeText(requireContext(), "Please log in to save favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addFavorite(userId: String, movie: Movie) {
        val favorite = hashMapOf(
            "movieId" to movie.id,
            "title" to movie.title,
            "posterUrl" to movie.poster_path,
            "adult" to movie.adult,
            "backdrop_path" to movie.backdrop_path,
            "genres" to movie.genres,
            "original_language" to movie.original_language,
            "original_title" to movie.original_title,
            "original_name" to movie.original_name,
            "name" to movie.name,
            "media_type" to movie.media_type,
            "overview" to movie.overview,
            "popularity" to movie.popularity,
            "release_date" to movie.release_date,
            "video" to movie.video,
            "vote_average" to movie.vote_average,
            "vote_count" to movie.vote_count,
            "tagline" to movie.tagline
        )

        Log.d("DetailFragment", "Adding favorite: $favorite for user: $userId")

        db.collection("users").document(userId)
            .collection("favourites").document(movie.id.toString())
            .set(favorite)
            .addOnSuccessListener {
                Log.d("DetailFragment", "Favorite added successfully")
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("DetailFragment", "Error adding favorite", e)
                Toast.makeText(requireContext(), "Failed to add favorite", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFavorite(userId: String, movieId: Int) {
        Log.d("DetailFragment", "Removing favorite for user: $userId, movieId: $movieId")

        db.collection("users").document(userId)
            .collection("favourites").document(movieId.toString())
            .delete()
            .addOnSuccessListener {
                Log.d("DetailFragment", "Favorite removed successfully")
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("DetailFragment", "Error removing favorite", e)
                Toast.makeText(requireContext(), "Failed to remove favorite", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleLikeButtonAnimation() {
        if (isLiked) {
            likeButton.speed = 1f
            likeButton.playAnimation()
        } else {
            likeButton.speed = -1f
            likeButton.playAnimation()
        }
    }


    private fun loadCast(movieId: Int) {
        val call = CastClient.getInstance().getMovieCast(movieId)
        call.enqueue(object : retrofit2.Callback<CastResponse> {
            override fun onResponse(call: retrofit2.Call<CastResponse>, response: retrofit2.Response<CastResponse>) {
                if (response.isSuccessful) {
                    val castList = response.body()?.cast ?: emptyList()
                    castAdapter = CastAdapter(castList) { personId ->
                        val bundle = Bundle().apply {
                            putInt("personId", personId)
                        }
                        findNavController().navigate(R.id.navigation_detailsperson, bundle)
                    }
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

    private fun fetchPosters(movieId: Int) {
        val call = ImagesClient.getInstance().getMovieImages(movieId, Constants.API_KEY)
        call.enqueue(object : Callback<ImagesResponse> {
            override fun onResponse(call: Call<ImagesResponse>, response: Response<ImagesResponse>) {
                if (response.isSuccessful) {
                    val images = response.body()?.posters ?: emptyList()
                    val posterUrls = images.map { "https://image.tmdb.org/t/p/w500${it.file_path}" }
                    Log.d("DetailFragment", "Poster URLs: $posterUrls")
                    postersRecyclerView.adapter = PosterAdapter(posterUrls)
                } else {
                    Log.e("DetailFragment", "Error al obtener las imágenes: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error al obtener las imágenes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                Log.e("DetailFragment", "Error en la llamada de red: ${t.message}")
                Toast.makeText(requireContext(), "Error en la llamada de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCrew(movieId: Int) {
        val call = CrewClient.getInstance().getMovieCrew(movieId)
        call.enqueue(object : retrofit2.Callback<CrewResponse> {
            override fun onResponse(call: retrofit2.Call<CrewResponse>, response: retrofit2.Response<CrewResponse>) {
                if (response.isSuccessful) {
                    val crewList = response.body()?.crew ?: emptyList()
                    crewAdapter = CrewAdapter(crewList) { personId ->
                        val bundle = Bundle().apply {
                            putInt("personId", personId)
                        }
                        findNavController().navigate(R.id.navigation_detailsperson, bundle)
                    }
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

    private fun fetchReviews(movieId: Int) {
        val call = ReviewClient.getInstance().getMovieReviews(movieId, Constants.API_KEY)
        call.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful) {
                    val reviewList = response.body()?.results ?: emptyList()
                    val reviewsAdapter = ReviewAdapter(reviewList)
                    reviewsRecyclerView.adapter = reviewsAdapter

                    if (reviewList.isNotEmpty()) {
                        showReviewsButton.visibility = View.VISIBLE
                    }
                } else {
                    Log.e("DetailFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
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

    private fun toggleReviewsVisibility() {
        if (reviewsVisible) {
            hideReviewsContainer()
        } else {
            showReviewsContainer()
        }
    }

    private fun showReviewsContainer() {
        reviewsOverlay.visibility = View.VISIBLE
        reviewsContainer.visibility = View.VISIBLE
        reviewsContainer.translationY = reviewsContainer.height.toFloat()
        reviewsContainer.animate()
            .translationY(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(600)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    reviewsVisible = true
                }
            })
            .start()
    }

    private fun hideReviewsContainer() {
        reviewsContainer.animate()
            .translationY(reviewsContainer.height.toFloat())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(600)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    reviewsContainer.visibility = View.GONE
                    reviewsOverlay.visibility = View.GONE
                    reviewsVisible = false
                }
            })
            .start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupOutsideTouchListener() {
        reviewsOverlay.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && reviewsVisible) {
                hideReviewsContainer()
                true
            } else {
                false
            }
        }
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
