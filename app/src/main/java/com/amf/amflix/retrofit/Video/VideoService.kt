package com.amf.amflix.retrofit.Video

import com.amf.amflix.retrofit.models.Video.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoService {
    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<VideoResponse>
}