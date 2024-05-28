package com.amf.amflix.ui.Search

import com.amf.amflix.retrofit.models.movies.PopularMoviesResponse
import com.amf.amflix.retrofit.models.series.TopTVSeriesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @GET("discover/movie")
    fun getFilteredMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("with_genres") genre: String?,
        @Query("primary_release_year") year: String?,
        @Query("sort_by") sortBy: String?,
        @Query("type") type: String?
    ): Call<PopularMoviesResponse>

    @GET("discover/tv")
    fun getFilteredSeries(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genre: String?,
        @Query("first_air_date_year") year: String?,
        @Query("sort_by") sortBy: String?
    ): Call<TopTVSeriesResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
