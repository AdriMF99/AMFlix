package com.amf.amflix.ui.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initializeViews(view: View) {
        popularMoviesRecyclerView = view.findViewById(R.id.popular_movies_recycler_view)
        topRatedMoviesRecyclerView = view.findViewById(R.id.top_rated_movies_recycler_view)
        trendingMoviesRecyclerView = view.findViewById(R.id.trending_movies_recycler_view)
        popularTvShowsRecyclerView = view.findViewById(R.id.popular_tv_shows_recycler_view)
        topRatedTvShowsRecyclerView = view.findViewById(R.id.top_rated_tv_shows_recycler_view)
        trendingTvShowsRecyclerView = view.findViewById(R.id.trending_tv_shows_recycler_view)

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
