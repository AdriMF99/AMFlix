package com.amf.amflix.retrofit.Cast


import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Cast.Cast
import com.amf.amflix.retrofit.models.Cast.CastInterceptor
import com.amf.amflix.retrofit.models.Cast.CastResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CastClient private constructor() {
    private val castService: CastService
    private val retrofit: Retrofit

    companion object {
        @Volatile
        private var instance: CastClient? = null

        fun getInstance(): CastClient {
            return instance ?: synchronized(this) {
                instance ?: CastClient().also { instance = it }
            }
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(CastInterceptor())

        val client = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        castService = retrofit.create(CastService::class.java)
    }

    fun getMovieCast(movieId: Int): retrofit2.Call<CastResponse> {
        return castService.getMovieCredits(movieId)
    }
    fun getTvShowCast(movieId: Int): retrofit2.Call<CastResponse> {
        return castService.getSeriesCredits(movieId)
    }
}