package com.amf.amflix.retrofit.Crew

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Crew.CrewInterceptor
import com.amf.amflix.retrofit.models.Crew.CrewResponse

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrewClient private constructor() {
    private val crewService: CrewService
    private val retrofit: Retrofit

    companion object {
        @Volatile
        private var instance: CrewClient? = null

        fun getInstance(): CrewClient {
            return instance ?: synchronized(this) {
                instance ?: CrewClient().also { instance = it }
            }
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(CrewInterceptor())

        val client = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        crewService = retrofit.create(CrewService::class.java)
    }

    fun getMovieCrew(movieId: Int): retrofit2.Call<CrewResponse> {
        return crewService.getMovieCredits(movieId)
    }
    fun getTvShowCrew(movieId: Int): retrofit2.Call<CrewResponse> {
        return crewService.getSeriesCredits(movieId)
    }
}