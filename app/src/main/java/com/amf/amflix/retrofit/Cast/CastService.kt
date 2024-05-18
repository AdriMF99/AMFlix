package com.amf.amflix.retrofit.Cast

import com.amf.amflix.retrofit.models.Cast.CastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CastService {
    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") movieId: Int): Call<CastResponse>

    @GET("tv/{movie_id}/credits")
    fun getSeriesCredits(@Path("movie_id") movieId: Int): Call<CastResponse>
}