package com.amf.amflix.ui.Search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/multi")
    fun searchMoviesAndSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<SearchResponse>
}
