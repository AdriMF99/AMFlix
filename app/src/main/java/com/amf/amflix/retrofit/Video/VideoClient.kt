package com.amf.amflix.retrofit.Video

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.Cast.CastResponse
import com.amf.amflix.retrofit.models.Video.VideoResponse
import com.amf.amflix.retrofit.models.movies.MovieInterceptor
import com.amf.amflix.retrofit.movies.MovieClient
import com.amf.amflix.retrofit.movies.MovieService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoClient {
    val videoService: VideoService

    companion object {
        @Volatile private var instance: VideoClient? = null

        fun getInstance(): VideoClient {
            return instance ?: synchronized(this) {
                instance ?: VideoClient().also { instance = it }
            }
        }
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(MovieInterceptor())

        val client = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        videoService = retrofit.create(VideoService::class.java)
    }
}