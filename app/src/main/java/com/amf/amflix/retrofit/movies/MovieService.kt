package com.amf.amflix.retrofit.movies

import com.amf.amflix.retrofit.models.Cast.Cast
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.movies.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/popular")
    fun getPopularMovies(): retrofit2.Call<PopularMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): retrofit2.Call<PopularMoviesResponse>

    @GET("trending/movie/day")
    fun getTrendingMovies(): retrofit2.Call<PopularMoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int): retrofit2.Call<Movie>

    @GET("{type}")
    fun getTypeMovies(@Path("type") type: String): retrofit2.Call<PopularMoviesResponse>
}