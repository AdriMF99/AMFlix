package com.amf.amflix.ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amf.amflix.repository.movies.MovieRepository
import com.amf.amflix.retrofit.models.movies.Movie
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val movieRepository = MovieRepository()

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> get() = _topRatedMovies

    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> get() = _trendingMovies

    private var _selected:Movie?=null

    var selectedMovie: Movie? = null

    init {
        fetchPopularMovies()
        fetchTopRatedMovies()
        fetchTrendingMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            movieRepository.popularMovies()?.observeForever { movies ->
                _popularMovies.postValue(movies)
            }
        }
    }

    private fun fetchTopRatedMovies() {
        viewModelScope.launch {
            movieRepository.getTopRatedMovies()?.observeForever { movies ->
                _topRatedMovies.postValue(movies)
            }
        }
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            movieRepository.getTrendingMovies()?.observeForever { movies ->
                _trendingMovies.postValue(movies)
            }
        }
    }
}