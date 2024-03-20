package com.amf.amflix.repository.series

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.amf.amflix.common.App
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.retrofit.models.series.TopTVSeriesResponse
import com.amf.amflix.retrofit.series.TVSeriesClient
import com.amf.amflix.retrofit.series.TVSeriesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVSeriesRepository {
    var tvSeriesService: TVSeriesService? = null
    var tvSeriesClient: TVSeriesClient? = null
    var topTVSeries: MutableLiveData<List<TVSeries>> ?= null

    init {
        tvSeriesClient = TVSeriesClient.instance
        tvSeriesService = tvSeriesClient?.getTVSeriesService()
        topTVSeries = topTVSeries()
    }

    fun topTVSeries(): MutableLiveData<List<TVSeries>>?{
        if (topTVSeries == null){
            topTVSeries = MutableLiveData<List<TVSeries>>()
        }
        val call: Call<TopTVSeriesResponse>? = tvSeriesService?.getTopTVSeries()
        call?.enqueue(object : Callback<TopTVSeriesResponse>{
            override fun onResponse(
                call: Call<TopTVSeriesResponse>,
                response: Response<TopTVSeriesResponse>
            ) {
                if (response.isSuccessful){
                    topTVSeries?.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TopTVSeriesResponse>, t: Throwable) {
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }

        })

        return topTVSeries
    }

}