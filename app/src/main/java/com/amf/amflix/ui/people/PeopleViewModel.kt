package com.amf.amflix.ui.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amf.amflix.repository.people.PeopleRepository
import com.amf.amflix.retrofit.models.people.People

class PeopleViewModel: ViewModel() {
    private var peopleRepository: PeopleRepository
    private var popularPeople: LiveData<List<People>>

    init {
        peopleRepository = PeopleRepository()
        popularPeople = peopleRepository?.popularPeople()!!
    }

    fun getPopularPeople(): LiveData<List<People>>{
        return popularPeople
    }
}