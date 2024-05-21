package com.amf.amflix.ui.series

import android.animation.ValueAnimator
import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.Cast.CastClient
import com.amf.amflix.retrofit.Video.VideoClient
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.amf.amflix.retrofit.models.Video.Video
import com.amf.amflix.retrofit.models.Video.VideoResponse
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.retrofit.series.TVSeriesClient
import com.amf.amflix.ui.Video.VideoPlayerDialogFragment
import com.amf.amflix.ui.movies.CastAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvDetailFragment : Fragment() {
    private val tvShowViewModel: TvSeriesViewModel by activityViewModels()
    private lateinit var v: View
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var castAdapter: CastAdapter
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
    private lateinit var playTrailerButton: FloatingActionButton
    private lateinit var genresContainer: LinearLayout
    private lateinit var tag: TextView

    var tvShowClient: TVSeriesClient?= null


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
        backdrop = v.findViewById(R.id.backgroundImage)
        castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        playTrailerButton = v.findViewById(R.id.playTrailerButton)
        genresContainer = v.findViewById(R.id.genresContainer)
        tag = v.findViewById(R.id.taglinetv)
        tvShowClient = TVSeriesClient.instance

        // OnClickListener para cambiar el tamaño de la imagen
        tvPoster.setOnClickListener {
            toggleSize(tvPoster)
        }

        playTrailerButton.setOnClickListener {
            showVideoOptionsDialog()
        }
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
        initializeCurrentTvShowVideos(tvShow.id)
    }


    private fun loadCast(tvShowId: Int) {
        val call = CastClient.getInstance().getTvShowCast(tvShowId)
        call.enqueue(object : retrofit2.Callback<CastResponse> {
            override fun onResponse(call: retrofit2.Call<CastResponse>, response: retrofit2.Response<CastResponse>) {
                if (response.isSuccessful) {
                    val castList = response.body()?.cast ?: emptyList()
                    castAdapter = CastAdapter(castList)
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
            val videoTitles = currentTvShowVideos.map { it.name }.toTypedArray()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select a Video")
                .setItems(videoTitles) { _, which ->
                    val selectedVideo = currentTvShowVideos[which]
                    val dialogFragment = VideoPlayerDialogFragment(selectedVideo.key)
                    dialogFragment.show(parentFragmentManager, "videoPlayerDialog")
                }
            val dialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(requireContext(), "No videos available :(", Toast.LENGTH_SHORT).show()
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
        fun newInstance(): TvDetailFragment {
            return TvDetailFragment()
        }
    }
}