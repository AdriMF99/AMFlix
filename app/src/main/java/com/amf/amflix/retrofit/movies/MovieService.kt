package com.amf.amflix.retrofit.movies

import com.amf.amflix.retrofit.models.cast.MovieCredits
import com.amf.amflix.retrofit.models.movies.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    /**
     * Define el método que se utilizará para realizar solicitudes a la API de TheMovieDB.
     * En este caso, el método getPopularMovies() se encarga de obtener películas populares.
     *
     * @author DaveDev117
     * @version 1.0
     * @since 10/10/2023
     */

    @GET("movie/top_rated")
    fun getPopularMovies(): retrofit2.Call<PopularMoviesResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") movieId: Int): MovieCredits
}