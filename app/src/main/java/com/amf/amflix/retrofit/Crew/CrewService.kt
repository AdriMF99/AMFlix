package com.amf.amflix.retrofit.Crew

import com.amf.amflix.retrofit.models.Crew.CrewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CrewService {
    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") movieId: Int): Call<CrewResponse>

    @GET("tv/{movie_id}/credits")
    fun getSeriesCredits(@Path("movie_id") movieId: Int): Call<CrewResponse>
}