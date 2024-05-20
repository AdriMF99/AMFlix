package com.amf.amflix.retrofit.movies

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.movies.MovieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieClient {

    private val movieService: MovieService
    private val retrofit: Retrofit

    companion object{
        var instance: MovieClient? = null
            get() {
                if (field == null){
                    instance = MovieClient()
                }
                return field
            }
    }

    fun getInstance(): MovieClient {
        return MovieClient.instance ?: synchronized(this) {
            MovieClient.instance ?: MovieClient().also { MovieClient.instance = it }
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(MovieInterceptor())

        val client = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        movieService = retrofit.create(MovieService::class.java)
    }

    fun getMovieService() = movieService
    fun getMovieDetails(movieId: Int): retrofit2.Call<Movie> {
        return movieService.getMovieDetails(movieId)
    }
}