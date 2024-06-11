package com.amf.amflix.ui.Search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.amf.amflix.R
import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.movies.PopularMoviesResponse
import com.amf.amflix.retrofit.models.series.TVSeries
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private lateinit var adapter: SearchAdapter
    private lateinit var adaptertv: SearchAdaptertv
    private lateinit var searchView: SearchView
    private val movieviewmodel: com.amf.amflix.ui.movies.MovieViewModel by activityViewModels()
    private val tvseriesviewmodel: com.amf.amflix.ui.series.TvSeriesViewModel by activityViewModels()
    private var currentPage = 1
    private var isLoading = false

    private lateinit var searchButton: Button
    private lateinit var filterButton: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val view2 = inflater.inflate(R.layout.bottom_sheet_filters, container, false)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        searchView = view.findViewById(R.id.searchView)
        filterButton = view.findViewById(R.id.filter_button)
        searchButton = view2.findViewById(R.id.apply_filters_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

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

        recyclerView.layoutManager = GridLayoutManager(context, 2)
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

        filterButton.setOnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_filters, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(bottomSheetView)

            val typeSpinner: Spinner = bottomSheetView.findViewById(R.id.type_spinner)
            val yearInput: EditText = bottomSheetView.findViewById(R.id.year_input)
            val sortSpinner: Spinner = bottomSheetView.findViewById(R.id.sort_spinner)
            val genreSpinner: Spinner = bottomSheetView.findViewById(R.id.genre_spinner)
            val applyFiltersButton: Button = bottomSheetView.findViewById(R.id.apply_filters_button)

            applyFiltersButton.setOnClickListener {
                val type = typeSpinner.selectedItem.toString().lowercase()
                val year = yearInput.text.toString()
                val sortBy = when (sortSpinner.selectedItem.toString()) {
                    "Popularity" -> "popularity.desc"
                    "Release Date" -> if (type == "movie") "release_date.desc" else "first_air_date.desc"
                    "Rating" -> "vote_average.desc"
                    else -> "popularity.desc"
                }
                val genre = when (genreSpinner.selectedItem.toString()) {
                    "Action" -> if (type == "movie") "28" else "10759"
                    "Animation" -> "16"
                    "Comedy" -> "35"
                    "Crime" -> "80"
                    "Documentary" -> "99"
                    "Drama" -> "18"
                    "Family" -> "10751"
                    "Fantasy" -> if (type == "movie") "14" else "10765"
                    "History" -> if (type == "movie") "36" else ""
                    "Horror" -> if (type == "movie") "27" else ""
                    "Music" -> if (type == "movie") "10402" else ""
                    "Mystery" -> "9648"
                    "Romance" -> if (type == "movie") "10749" else ""
                    "Science Fiction" -> if (type == "movie") "878" else "10765"
                    "TV Movie" -> if (type == "movie") "10770" else ""
                    "Thriller" -> if (type == "movie") "53" else ""
                    "War" -> if (type == "movie") "10752" else "10768"
                    "Western" -> "37"
                    else -> ""
                }

                val radioGroup = view?.findViewById<RadioGroup>(R.id.radioGroup)
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

                if (type == "movie") {
                    radioGroup?.check(R.id.radioButtonMovies)
                    viewModel.fetchFilteredMovies(searchView.query.toString() ,genre, year, sortBy, type)
                    recyclerView?.adapter = adapter
                } else {
                    radioGroup?.check(R.id.radioButtonSeries)
                    recyclerView?.adapter = adaptertv
                    viewModel.fetchFilteredSeries(genre, year, sortBy)
                }

                dialog.dismiss()
            }

            dialog.show()
        }

        return view
    }

    private fun observeMovies() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (isAdded) {
                adapter.setItems(results)
            }
        }
    }

    private fun observeSeries() {
        viewModel.searchResultstv.observe(viewLifecycleOwner) { results ->
            if (isAdded) {
                adaptertv.setItems(results)
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
                }
                return true
            }
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

    private fun loadNextPage(isMovie: Boolean) {
        currentPage++
        if (isMovie) {
            viewModel.search(searchView.query.toString(), currentPage)
        } else {
            viewModel.searchtv(searchView.query.toString(), currentPage)
        }
    }
}
