package com.amf.amflix.retrofit.Images

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Images.ImagesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImagesService {
    @GET("movie/{movie_id}/images")
    fun getMovieImages(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<ImagesResponse>

    @GET("tv/{series_id}/images")
    fun getSeriesImages(@Path("series_id") seriesId: Int, @Query("api_key") apiKey: String): Call<ImagesResponse>
}

object ImagesClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getInstance(): ImagesService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ImagesService::class.java)
    }
}
