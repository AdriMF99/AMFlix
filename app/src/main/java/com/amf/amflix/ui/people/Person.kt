package com.amf.amflix.ui.people

import com.amf.amflix.retrofit.models.people.KnownFor

data class Person (
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
    val known_for: List<KnownFor>
)