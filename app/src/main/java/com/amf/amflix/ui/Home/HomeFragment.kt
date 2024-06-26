package com.amf.amflix.ui.Home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.ui.movies.Type
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private val movieViewModel: MovieViewModel by activityViewModels()
    private val movieviewmodel: com.amf.amflix.ui.movies.MovieViewModel by activityViewModels()
    private val tvSeriesViewModel: TvSeriesViewModel by activityViewModels()
    private val tvseriesviewmodel: com.amf.amflix.ui.series.TvSeriesViewModel by activityViewModels()

    private lateinit var popularMoviesRecyclerView: RecyclerView
    private lateinit var topRatedMoviesRecyclerView: RecyclerView
    private lateinit var trendingMoviesRecyclerView: RecyclerView
    private lateinit var popularTvShowsRecyclerView: RecyclerView
    private lateinit var topRatedTvShowsRecyclerView: RecyclerView
    private lateinit var trendingTvShowsRecyclerView: RecyclerView

    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var topRatedMoviesAdapter: MovieAdapter
    private lateinit var trendingMoviesAdapter: MovieAdapter
    private lateinit var popularTvShowsAdapter: TvShowAdapter
    private lateinit var topRatedTvShowsAdapter: TvShowAdapter
    private lateinit var trendingTvShowsAdapter: TvShowAdapter

    private lateinit var morePopular: TextView
    private lateinit var moreTop: TextView
    private lateinit var moreTrend: TextView
    private lateinit var morePopulartv: TextView
    private lateinit var moreToptv: TextView
    private lateinit var moreTrendtv: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            autoScrollRecyclerView(popularMoviesRecyclerView)
            handler.postDelayed(this, 50)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initializeViews(view)
        showBottomNavigation()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        handler.postDelayed(autoScrollRunnable, 1500)

        // Según dónde pulses se mostrarán una tipo de películas o series.
        morePopular.setOnClickListener {
            Type.tipopeli = "movie/popular"
            findNavController().navigate(R.id.navigation_movies)
        }

        moreTop.setOnClickListener {
            Type.tipopeli = "movie/top_rated"
            findNavController().navigate(R.id.navigation_movies)
        }

        moreTrend.setOnClickListener {
            Type.tipopeli = "trending/movie/day"
            findNavController().navigate(R.id.navigation_movies)
        }

        morePopulartv.setOnClickListener {
            Type.tipopeli = "tv/popular"
            findNavController().navigate(R.id.navigation_series)
        }

        moreToptv.setOnClickListener {
            Type.tipopeli = "tv/top_rated"
            findNavController().navigate(R.id.navigation_series)
        }

        moreTrendtv.setOnClickListener {
            Type.tipopeli = "trending/tv/day"
            findNavController().navigate(R.id.navigation_series)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(autoScrollRunnable)
    }

    private fun initializeViews(view: View) {
        popularMoviesRecyclerView = view.findViewById(R.id.popular_movies_recycler_view)
        topRatedMoviesRecyclerView = view.findViewById(R.id.top_rated_movies_recycler_view)
        trendingMoviesRecyclerView = view.findViewById(R.id.trending_movies_recycler_view)
        popularTvShowsRecyclerView = view.findViewById(R.id.popular_tv_shows_recycler_view)
        topRatedTvShowsRecyclerView = view.findViewById(R.id.top_rated_tv_shows_recycler_view)
        trendingTvShowsRecyclerView = view.findViewById(R.id.trending_tv_shows_recycler_view)
        morePopular = view.findViewById(R.id.morePopular)
        moreTop = view.findViewById(R.id.moreTop)
        moreTrend = view.findViewById(R.id.moreTrend)
        morePopulartv = view.findViewById(R.id.morePopulartv)
        moreToptv = view.findViewById(R.id.moreToptv)
        moreTrendtv = view.findViewById(R.id.moreTrendtv)

        setupRecyclerView(popularMoviesRecyclerView)
        setupRecyclerView(topRatedMoviesRecyclerView)
        setupRecyclerView(trendingMoviesRecyclerView)
        setupRecyclerView(popularTvShowsRecyclerView)
        setupRecyclerView(topRatedTvShowsRecyclerView)
        setupRecyclerView(trendingTvShowsRecyclerView)

        popularMoviesAdapter = MovieAdapter { movie -> navigateToMovieDetail(movie) }
        topRatedMoviesAdapter = MovieAdapter { movie -> navigateToMovieDetail(movie) }
        trendingMoviesAdapter = MovieAdapter { movie -> navigateToMovieDetail(movie) }
        popularTvShowsAdapter = TvShowAdapter { tvShow -> navigateToTvShowDetail(tvShow) }
        topRatedTvShowsAdapter = TvShowAdapter { tvShow -> navigateToTvShowDetail(tvShow) }
        trendingTvShowsAdapter = TvShowAdapter { tvShow -> navigateToTvShowDetail(tvShow) }

        popularMoviesRecyclerView.adapter = popularMoviesAdapter
        topRatedMoviesRecyclerView.adapter = topRatedMoviesAdapter
        trendingMoviesRecyclerView.adapter = trendingMoviesAdapter
        popularTvShowsRecyclerView.adapter = popularTvShowsAdapter
        topRatedTvShowsRecyclerView.adapter = topRatedTvShowsAdapter
        trendingTvShowsRecyclerView.adapter = trendingTvShowsAdapter
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupObservers() {
        movieViewModel.popularMovies.observe(viewLifecycleOwner, Observer { movies ->
            popularMoviesAdapter.submitList(movies)
        })

        movieViewModel.topRatedMovies.observe(viewLifecycleOwner, Observer { movies ->
            topRatedMoviesAdapter.submitList(movies)
        })

        movieViewModel.trendingMovies.observe(viewLifecycleOwner, Observer { movies ->
            trendingMoviesAdapter.submitList(movies)
        })

        tvSeriesViewModel.popularTvShows.observe(viewLifecycleOwner, Observer { tvShows ->
            popularTvShowsAdapter.submitList(tvShows)
        })

        tvSeriesViewModel.topRatedTvShows.observe(viewLifecycleOwner, Observer { tvShows ->
            topRatedTvShowsAdapter.submitList(tvShows)
        })

        tvSeriesViewModel.trendingTvShows.observe(viewLifecycleOwner, Observer { tvShows ->
            trendingTvShowsAdapter.submitList(tvShows)
        })
    }

    // No se puede pulsar en una película del carrusel en movimiento, pero queda bonito.
    private fun autoScrollRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
        val itemCount = layoutManager.itemCount
        recyclerView.smoothScrollBy(5, 0) // Ajusta la distancia del desplazamiento para un desplazamiento más suave
        if (lastVisibleItemPosition == itemCount - 1) {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        movieviewmodel.selected = movie
        findNavController().navigate(R.id.navigation_details)
    }

    private fun navigateToTvShowDetail(tvShow: TVSeries) {
        tvseriesviewmodel.selected = tvShow
        findNavController().navigate(R.id.navigation_tvdetails)
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.clearAnimation() // Detener la animación
        bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.clearAnimation() // Detener la animación
        bottomNavigationView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
