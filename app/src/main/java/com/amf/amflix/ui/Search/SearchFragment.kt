package com.amf.amflix.ui.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private lateinit var adapter: SearchAdapter
    private lateinit var adaptertv: SearchAdaptertv
    private lateinit var searchView: SearchView
    private lateinit var animationView: LottieAnimationView
    private val movieviewmodel: com.amf.amflix.ui.movies.MovieViewModel by activityViewModels()
    private val tvseriesviewmodel: com.amf.amflix.ui.series.TvSeriesViewModel by activityViewModels()
    private var currentPage = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        searchView = view.findViewById(R.id.searchView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        animationView = view.findViewById(R.id.animationView)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonMovies -> {
                    recyclerView.adapter = adapter
                    observeMovies()
                    viewModel.clearSearchResults()
                    if (!searchView.query.isNullOrEmpty()) {
                        viewModel.search(searchView.query.toString())
                    }
                    setupSearchView(isMovie = true)
                }
                R.id.radioButtonSeries -> {
                    recyclerView.adapter = adaptertv
                    observeSeries()
                    viewModel.clearSearchResults()
                    if (!searchView.query.isNullOrEmpty()) {
                        viewModel.searchtv(searchView.query.toString())
                    }
                    setupSearchView(isMovie = false)
                }
            }
        }

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = SearchAdapter { movie -> navigateToMovieDetail(movie) }
        adaptertv = SearchAdaptertv { serie -> navigateToTvShowDetail(serie) }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && lastVisiblePosition == adapter.itemCount - 1) {
                    loadNextPage(isMovie = recyclerView.adapter == adapter)
                }
            }
        })

        return view
    }

    private fun observeMovies() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (isAdded) {
                adapter.setItems(results)
                toggleAnimation(results.isEmpty())
            }
        }
    }

    private fun observeSeries() {
        viewModel.searchResultstv.observe(viewLifecycleOwner) { results ->
            if (isAdded) {
                adaptertv.setItems(results)
                toggleAnimation(results.isEmpty())
            }
        }
    }

    private fun setupSearchView(isMovie: Boolean) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    viewModel.clearSearchResults()
                    if (isMovie) {
                        viewModel.search(newText)
                    } else {
                        viewModel.searchtv(newText)
                    }
                } else {
                    if (isMovie) {
                        adapter.setItems(emptyList())
                    } else {
                        adaptertv.setItems(emptyList())
                    }
                    toggleAnimation(true)
                }
                return true
            }
        })
    }

    private fun toggleAnimation(show: Boolean) {
        animationView.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToMovieDetail(movie: Movie) {
        movieviewmodel.selected = movie
        findNavController().navigate(R.id.navigation_details)
    }

    private fun navigateToTvShowDetail(tvShow: TVSeries) {
        tvseriesviewmodel.selected = tvShow
        findNavController().navigate(R.id.navigation_tvdetails)
    }

    private fun loadNextPage(isMovie: Boolean) {
        currentPage++
        if (isMovie) {
            viewModel.search(searchView.query.toString(), currentPage)
        } else {
            viewModel.searchtv(searchView.query.toString(), currentPage)
        }
    }
}
