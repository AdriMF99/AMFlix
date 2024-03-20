package com.amf.amflix.retrofit.series

import com.amf.amflix.retrofit.models.series.TopTVSeriesResponse
import retrofit2.http.GET

interface TVSeriesService {
    @GET("tv/top_rated")
    fun getTopTVSeries(): retrofit2.Call<TopTVSeriesResponse>
}