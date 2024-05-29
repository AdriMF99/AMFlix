package com.amf.amflix.ui.Search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    fun searchMoviesAndSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Call<SearchResponse>

    @GET("search/tv")
    fun searchSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Call<SearchResponsetv>
}
