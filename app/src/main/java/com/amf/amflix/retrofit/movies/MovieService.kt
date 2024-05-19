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

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") movieId: Int, ) : retrofit2.Call<CastResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int): retrofit2.Call<Movie>
}