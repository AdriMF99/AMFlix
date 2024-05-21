package com.amf.amflix.repository.series

import android.widget.Toast
import androidx.lifecycle.LiveData
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

    var seriesService: TVSeriesService? = null
    var seriesClient: TVSeriesClient?= null

    var popularSeries: MutableLiveData<List<TVSeries>> ?= null

    // Inicialización de propiedades
    init {
        seriesClient = TVSeriesClient.instance
        seriesService = seriesClient?.getSeriesService()
        popularSeries = popularSeries()
    }

    fun popularSeries(): MutableLiveData<List<TVSeries>>?{
        if (popularSeries == null){
            popularSeries = MutableLiveData<List<TVSeries>>()
        }
        // Obtiene las películas populares de la API
        val call: Call<TopTVSeriesResponse>? = seriesService?.getTopTVSeries()
        call?.enqueue(object : Callback<TopTVSeriesResponse>{
            override fun onResponse(
                call: Call<TopTVSeriesResponse>,
                response: Response<TopTVSeriesResponse>
            ) {
                if (response.isSuccessful) {
                    // Actualiza el objeto popularSeries con los resultados
                    popularSeries?.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TopTVSeriesResponse>, t: Throwable) {
                // Muestra un mensaje de Toast en caso de que la solicitud falle
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }

        })

        return popularSeries
    }

    fun getPopularSeries(): LiveData<List<TVSeries>> {
        val popularSeries = MutableLiveData<List<TVSeries>>()

        val call: Call<TopTVSeriesResponse>? = seriesService?.getPopularSeries()
        call?.enqueue(object : Callback<TopTVSeriesResponse> {
            override fun onResponse(call: Call<TopTVSeriesResponse>, response: Response<TopTVSeriesResponse>) {
                if (response.isSuccessful) {
                    popularSeries.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TopTVSeriesResponse>, t: Throwable) {
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })

        return popularSeries
    }

    fun getTopRatedTvShows(): LiveData<List<TVSeries>> {
        val topRatedSeries = MutableLiveData<List<TVSeries>>()

        val call: Call<TopTVSeriesResponse>? = seriesService?.getTopRatedSeries()
        call?.enqueue(object : Callback<TopTVSeriesResponse> {
            override fun onResponse(call: Call<TopTVSeriesResponse>, response: Response<TopTVSeriesResponse>) {
                if (response.isSuccessful) {
                    topRatedSeries.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TopTVSeriesResponse>, t: Throwable) {
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })

        return topRatedSeries
    }

    fun getTrendingTvShows(): LiveData<List<TVSeries>> {
        val trendingSeries = MutableLiveData<List<TVSeries>>()

        val call: Call<TopTVSeriesResponse>? = seriesService?.getTrendingSeries()
        call?.enqueue(object : Callback<TopTVSeriesResponse> {
            override fun onResponse(call: Call<TopTVSeriesResponse>, response: Response<TopTVSeriesResponse>) {
                if (response.isSuccessful) {
                    trendingSeries.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TopTVSeriesResponse>, t: Throwable) {
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })

        return trendingSeries
    }
}