package com.amf.amflix.repository.movies

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.amf.amflix.common.App
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.movies.PopularMoviesResponse
import com.amf.amflix.retrofit.movies.MovieClient
import com.amf.amflix.retrofit.movies.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {

    var movieService: MovieService? = null
    var movieClient: MovieClient?= null

    var popularMovies: MutableLiveData<List<Movie>> ?= null

    // Inicialización de propiedades
    init {
        movieClient = MovieClient.instance
        movieService = movieClient?.getMovieService()
        popularMovies = popularMovies()
    }

    fun popularMovies(): MutableLiveData<List<Movie>>?{
        if (popularMovies == null){
            popularMovies = MutableLiveData<List<Movie>>()
        }
        // Obtiene las películas populares de la API
        val call: Call<PopularMoviesResponse>? = movieService?.getPopularMovies()
        call?.enqueue(object : Callback<PopularMoviesResponse>{
            override fun onResponse(
                call: Call<PopularMoviesResponse>,
                response: Response<PopularMoviesResponse>
            ) {
                if (response.isSuccessful) {
                    // Actualiza el objeto popularMovies con los resultados
                    popularMovies?.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularMoviesResponse>, t: Throwable) {
                // Muestra un mensaje de Toast en caso de que la solicitud falle
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }

        })

        return popularMovies
    }
}