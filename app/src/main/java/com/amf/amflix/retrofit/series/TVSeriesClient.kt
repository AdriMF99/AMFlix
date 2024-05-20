package com.amf.amflix.retrofit.series

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.series.TVSeries
import com.amf.amflix.retrofit.models.series.TVSeriesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TVSeriesClient {
    private val seriesService: TVSeriesService
    private val retrofit: Retrofit

    companion object{
        var instance: TVSeriesClient? = null
            get() {
                if (field == null){
                    instance = TVSeriesClient()
                }
                return field
            }
    }

    fun getInstance(): TVSeriesClient {
        return TVSeriesClient.instance ?: synchronized(this) {
            TVSeriesClient.instance ?: TVSeriesClient().also { TVSeriesClient.instance = it }
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(TVSeriesInterceptor())

        val client = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        seriesService = retrofit.create(TVSeriesService::class.java)
    }

    fun getSeriesService() = seriesService
    fun getTvShowDetails(seriesId: Int): retrofit2.Call<TVSeries> {
        return seriesService.getTvShowDetails(seriesId)
    }

}