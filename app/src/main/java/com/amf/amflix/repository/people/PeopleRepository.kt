package com.amf.amflix.repository.people

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.amf.amflix.common.App
import com.amf.amflix.retrofit.models.people.People
import com.amf.amflix.retrofit.models.people.PopularPeopleResponse
import com.amf.amflix.retrofit.people.PeopleClient
import com.amf.amflix.retrofit.people.PeopleService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeopleRepository {
    var peopleService: PeopleService? = null
    var peopleClient: PeopleClient? = null
    var popularPeople: MutableLiveData<List<People>> ? = null

    init {
        peopleClient = PeopleClient.instance
        peopleService = peopleClient?.getPeopleService()
        popularPeople = popularPeople()
    }

    fun popularPeople(): MutableLiveData<List<People>>?{
        if (popularPeople == null){
            popularPeople = MutableLiveData<List<People>>()
        }

        val call: Call<PopularPeopleResponse>? = peopleService?.getPopularPeople()
        call?.enqueue(object : Callback<PopularPeopleResponse>{
            override fun onResponse(
                call: Call<PopularPeopleResponse>,
                response: Response<PopularPeopleResponse>
            ) {
                if (response.isSuccessful){
                    popularPeople?.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularPeopleResponse>, t: Throwable) {
                Toast.makeText(App.instance, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show()
            }

        })

        return popularPeople

    }

}