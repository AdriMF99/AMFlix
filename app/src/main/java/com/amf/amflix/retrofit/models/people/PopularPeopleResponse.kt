package com.amf.amflix.retrofit.models.people

import com.amf.amflix.ui.people.Person

data class PopularPeopleResponse(
    val page: Int,
    val results: List<People>,
    val total_pages: Int,
    val total_results: Int
)

data class PersonResponse(
    val id: Int,
    val name: String?,
    val also_known_as: List<String>?,
    val biography: String?,
    val birthday: String?,
    val homepage: String?,
    val known_for_department: String?,
    val place_of_birth: String?,
    val popularity: Double?,
    val profile_path: String?,
    val known_for: List<KnownFor>?
)