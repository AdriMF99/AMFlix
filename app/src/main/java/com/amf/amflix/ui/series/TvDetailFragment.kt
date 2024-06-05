package com.amf.amflix.ui.series

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.retrofit.series.TVSeriesClient
import com.amf.amflix.ui.Video.VideoDialogFragment
import com.amf.amflix.ui.Video.VideoPlayerDialogFragment
import com.amf.amflix.ui.movies.CastAdapter
import com.amf.amflix.ui.movies.CrewAdapter
import com.amf.amflix.ui.movies.PosterAdapter
import com.amf.amflix.ui.movies.ReviewAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvDetailFragment : Fragment() {
    private val tvShowViewModel: TvSeriesViewModel by activityViewModels()
    private lateinit var v: View
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var castAdapter: CastAdapter
    private lateinit var tvcrewRecyclerView: RecyclerView
    private lateinit var tvcrewAdapter: CrewAdapter
    private lateinit var tvTitle: TextView
    private lateinit var originalTitle: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvPopularity: TextView
    private lateinit var tvVoteCount: TextView
    private lateinit var tvOverview: TextView
    private lateinit var tvPoster: PhotoView
    private lateinit var backdrop: ImageView
    private lateinit var flecha: ImageButton
    private lateinit var currentTvShowVideos: List<Video>
    private lateinit var playTrailerButton: LottieAnimationView
    private lateinit var genresContainer: LinearLayout
    private lateinit var tag: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var showReviewsButton: LottieAnimationView
    private lateinit var loveButton: LottieAnimationView
    private lateinit var reviewsContainer: LinearLayout
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsOverlay: FrameLayout
    private lateinit var postersRecyclerView: RecyclerView
    private var reviewsVisible: Boolean = false

    var tvShowClient: TVSeriesClient?= null

    private var isLiked = false

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    private var isExpanded = false
    private val smallWidth = 100 // dp
    private val smallHeight = 150 // dp
    private val largeWidth = 300 // dp
    private val largeHeight = 450 // dp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_tv_detail, container, false)
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
        initializeCurrentTvShowVideos(tvShowViewModel.selected?.id ?: return)
        showReviewsButton.setOnClickListener {
            toggleReviewsVisibility()
        }
        checkIfTvShowIsFavorite()
        setupLoveButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    private fun initializeViews() {
        tvTitle = v.findViewById(R.id.tvTitle)
        originalTitle = v.findViewById(R.id.originalTitle)
        tvReleaseDate = v.findViewById(R.id.tvReleaseDate)
        tvRating = v.findViewById(R.id.tvRating)
        tvPopularity = v.findViewById(R.id.tvPopularity)
        tvVoteCount = v.findViewById(R.id.tvVoteCount)
        tvOverview = v.findViewById(R.id.tvOverview)
        tvPoster = v.findViewById(R.id.tvThumbnail)
        flecha = v.findViewById(R.id.backarrow)
        castRecyclerView = v.findViewById(R.id.tvcast_list)
        tvcrewRecyclerView = v.findViewById(R.id.tvcrew_list)
        backdrop = v.findViewById(R.id.backgroundImage)
        castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tvcrewRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        playTrailerButton = v.findViewById(R.id.playTrailerButton)
        genresContainer = v.findViewById(R.id.genresContainer)
        tag = v.findViewById(R.id.taglinetv)
        tvShowClient = TVSeriesClient.instance
        ratingBar = v.findViewById(R.id.ratingBar)
        showReviewsButton = v.findViewById(R.id.showReviewsButton)
        playTrailerButton = v.findViewById(R.id.playTrailerButton)
        loveButton = v.findViewById(R.id.loveButton)
        reviewsContainer = v.findViewById(R.id.reviewsContainer)
        reviewsRecyclerView = v.findViewById(R.id.reviewsRecyclerView)
        reviewsOverlay = v.findViewById(R.id.reviewsOverlay)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postersRecyclerView = v.findViewById(R.id.postersRecyclerView)
        postersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // OnClickListener para cambiar el tamaño de la imagen
        tvPoster.setOnClickListener {
            toggleSize(tvPoster)
        }

        playTrailerButton.setOnClickListener {
            if (::currentTvShowVideos.isInitialized && currentTvShowVideos.isNotEmpty()) {
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
        val tvShow = tvShowViewModel.selected ?: return

        tvTitle.text = tvShow.name
        originalTitle.text = tvShow.original_name
        tvReleaseDate.text = tvShow.first_air_date
        tvRating.text = tvShow.vote_average.toString()
        tvPopularity.text = tvShow.popularity.toString()
        tvVoteCount.text = tvShow.vote_count.toString()
        tvOverview.text = tvShow.overview
        val movieRating = tvShow.vote_average / 2
        ratingBar.rating = movieRating.toFloat()

        // Carga la imagen de la portada utilizando Glide
        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL + tvShow.poster_path)
            .override(600, 900)
            .placeholder(R.drawable.placeholder_load)
            .error(R.drawable.error_image)
            .into(tvPoster)

        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL + tvShow.backdrop_path)
            .override(350, 200)
            .placeholder(R.drawable.placeholder_load)
            .error(R.drawable.error_image)
            .into(backdrop)

        // Llamar al servicio para obtener los detalles de la serie de televisión
        tvShowClient?.getTvShowDetails(tvShow.id)?.enqueue(object : Callback<TVSeries> {
            override fun onResponse(call: Call<TVSeries>, response: Response<TVSeries>) {
                if (response.isSuccessful) {
                    val tvShowDetails = response.body()
                    tvShowDetails?.genres?.let { genres ->
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
                    tvShowDetails?.tagline?.let { tagline ->
                        tag.text = tagline
                    }
                } else {
                    Log.e("TvDetailFragment", "Error al obtener los detalles de la serie de televisión: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TVSeries>, t: Throwable) {
                Log.e("TvDetailFragment", "Error al obtener los detalles de la serie de televisión: ${t.message}")
            }
        })


        loadCast(tvShow.id)
        loadCrew(tvShow.id)
        fetchReviews(tvShow.id)
        fetchPosters(tvShow.id)
        initializeCurrentTvShowVideos(tvShow.id)
    }

    private fun checkIfTvShowIsFavorite() {
        val user = auth.currentUser
        val tvShow = tvShowViewModel.selected ?: return

        if (user != null) {
            val userId = user.uid
            val docRef = db.collection("users").document(userId)
                .collection("tv_favorites").document(tvShow.id.toString())

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    isLiked = true
                    loveButton.progress = 1.0f // Asegúrate de que la animación de "like" esté completa
                } else {
                    isLiked = false
                    loveButton.progress = 0.0f // Asegúrate de que la animación de "like" no esté activada
                }
            }.addOnFailureListener { e ->
                Log.w("TvDetailFragment", "Error checking favorites", e)
            }
        }
    }

    private fun setupLoveButton() {
        loveButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                val userId = user.uid
                val tvShow = tvShowViewModel.selected ?: return@setOnClickListener

                if (isLiked) {
                    removeFavorite(userId, tvShow.id)
                } else {
                    addFavorite(userId, tvShow)
                }
                isLiked = !isLiked
                toggleLoveButtonAnimation()
            } else {
                Toast.makeText(requireContext(), "Please log in to save favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addFavorite(userId: String, tvSeries: TVSeries) {
        val favorite = hashMapOf(
            "id" to tvSeries.id,
            "backdrop_path" to tvSeries.backdrop_path,
            "first_air_date" to tvSeries.first_air_date,
            "genres" to tvSeries.genres,
            "media_type" to tvSeries.media_type,
            "name" to tvSeries.name,
            "origin_country" to tvSeries.origin_country,
            "original_language" to tvSeries.original_language,
            "original_name" to tvSeries.original_name,
            "overview" to tvSeries.overview,
            "popularity" to tvSeries.popularity,
            "poster_path" to tvSeries.poster_path,
            "vote_average" to tvSeries.vote_average,
            "vote_count" to tvSeries.vote_count,
            "tagline" to tvSeries.tagline
        )

        Log.d("TvDetailFragment", "Adding favorite: $favorite for user: $userId")

        db.collection("users").document(userId)
            .collection("tv_favorites").document(tvSeries.id.toString())
            .set(favorite)
            .addOnSuccessListener {
                Log.d("TvDetailFragment", "Favorite added successfully")
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("TvDetailFragment", "Error adding favorite", e)
                Toast.makeText(requireContext(), "Failed to add favorite", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFavorite(userId: String, tvShowId: Int) {
        Log.d("TvDetailFragment", "Removing favorite for user: $userId, tvShowId: $tvShowId")

        db.collection("users").document(userId)
            .collection("tv_favorites").document(tvShowId.toString())
            .delete()
            .addOnSuccessListener {
                Log.d("TvDetailFragment", "Favorite removed successfully")
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("TvDetailFragment", "Error removing favorite", e)
                Toast.makeText(requireContext(), "Failed to remove favorite", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleLoveButtonAnimation() {
        if (isLiked) {
            loveButton.speed = 1f
            loveButton.playAnimation()
        } else {
            loveButton.speed = -1f
            loveButton.playAnimation()
        }
    }


    private fun loadCast(tvShowId: Int) {
        val call = CastClient.getInstance().getTvShowCast(tvShowId)
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
                    Log.e("TvDetailFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<CastResponse>, t: Throwable) {
                Log.e("TvDetailFragment", "Error: ${t.message}")
            }
        })
    }

    private fun fetchPosters(seriesId: Int) {
        val call = ImagesClient.getInstance().getSeriesImages(seriesId, Constants.API_KEY)
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
        val call = CrewClient.getInstance().getTvShowCrew(movieId)
        call.enqueue(object : retrofit2.Callback<CrewResponse> {
            override fun onResponse(call: retrofit2.Call<CrewResponse>, response: retrofit2.Response<CrewResponse>) {
                if (response.isSuccessful) {
                    val crewList = response.body()?.crew ?: emptyList()
                    tvcrewAdapter = CrewAdapter(crewList)
                    tvcrewRecyclerView.adapter = tvcrewAdapter
                } else {
                    Log.e("DetailFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<CrewResponse>, t: Throwable) {
                Log.e("DetailFragment", "Error: ${t.message}")
            }
        })
    }

    private fun initializeCurrentTvShowVideos(tvShowId: Int) {
        lifecycleScope.launch {
            try {
                val call = VideoClient.getInstance().videoService.getTvShowVideos(tvShowId, Constants.API_KEY)
                call.enqueue(object : Callback<VideoResponse> {
                    override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                        if (response.isSuccessful) {
                            currentTvShowVideos = response.body()?.results?.filter { it.site == "YouTube" } ?: emptyList()
                        } else {
                            Log.e("TvDetailFragment", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                        Log.e("TvDetailFragment", "Error: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showVideoOptionsDialog() {
        if (::currentTvShowVideos.isInitialized && currentTvShowVideos.isNotEmpty()) {
            // Muestra el diálogo de selección de videos
            val dialogFragment = VideoDialogFragment(currentTvShowVideos)
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

    private fun fetchReviews(seriesId: Int) {
        val call = ReviewClient.getInstance().getSeriesReviews(seriesId, Constants.API_KEY)
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
        fun newInstance(): TvDetailFragment {
            return TvDetailFragment()
        }
    }
}
