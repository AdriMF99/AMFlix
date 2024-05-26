package com.amf.amflix.retrofit.Review

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Review.ReviewResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<ReviewResponse>

    @GET("tv/{series_id}/reviews")
    fun getSeriesReviews(@Path("series_id") seriesId: Int, @Query("api_key") apiKey: String): Call<ReviewResponse>
}

object ReviewClient {
    private var retrofit: Retrofit? = null

    fun getInstance(): ReviewService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ReviewService::class.java)
    }
}