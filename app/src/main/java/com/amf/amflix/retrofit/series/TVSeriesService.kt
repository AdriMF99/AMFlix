package com.amf.amflix.retrofit.series

import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.retrofit.models.series.TopTVSeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TVSeriesService {
    @GET("tv/popular")
    fun getTopTVSeries(): retrofit2.Call<TopTVSeriesResponse>

    @GET("tv/popular")
    fun getPopularSeries(): retrofit2.Call<TopTVSeriesResponse>

    @GET("tv/top_rated")
    fun getTopRatedSeries(): retrofit2.Call<TopTVSeriesResponse>

    @GET("trending/tv/day")
    fun getTrendingSeries(): retrofit2.Call<TopTVSeriesResponse>

    @GET("tv/{series_id}")
    fun getTvShowDetails(@Path("series_id") seriesId: Int): retrofit2.Call<TVSeries>
}
