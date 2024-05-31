package com.amf.amflix.retrofit.people

import com.amf.amflix.common.Constants
import com.amf.amflix.retrofit.models.people.PersonResponse
import com.amf.amflix.retrofit.models.people.PopularPeopleResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PeopleService {
    @GET("person/popular")
    fun getPopularPeople(): Call<PopularPeopleResponse>

    @GET("person/{person_id}")
    fun getPerson(@Path("person_id") person_id: Int): Call<PersonResponse>
}

object PersonClient {
    private var retrofit: Retrofit? = null

    fun getInstance(): PeopleService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(PeopleService::class.java)
    }
}
