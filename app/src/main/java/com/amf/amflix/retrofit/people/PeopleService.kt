package com.amf.amflix.retrofit.people

import com.amf.amflix.retrofit.models.people.PopularPeopleResponse
import retrofit2.http.GET

interface PeopleService {
    @GET("person/popular")
    fun getPopularPeople(): retrofit2.Call<PopularPeopleResponse>
}