package com.amf.amflix.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.repository.movies.MovieRepository
import com.amf.amflix.retrofit.models.movies.Movie

class MovieViewModel: ViewModel() {

    private var _movies: MutableList<Movie> = mutableListOf()
    private var _selected:Movie?=null

    val movies:List<Movie>
        get() = _movies.toList()
    var selected:Movie?
        get()=_selected
        set(item){ _selected=item}

    private var movieRepository: MovieRepository
    private var popularMovies: LiveData<List<Movie>>
    private var typeMovies: LiveData<List<Movie>>

    init {
        movieRepository = MovieRepository()
        popularMovies = movieRepository?.popularMovies()!!
        typeMovies = movieRepository?.getTypeMovies()!!
    }

    fun getPopularMovies(): LiveData<List<Movie>>{
        return popularMovies
    }

    fun getTypeMovies(): LiveData<List<Movie>>{
        return typeMovies
    }
}