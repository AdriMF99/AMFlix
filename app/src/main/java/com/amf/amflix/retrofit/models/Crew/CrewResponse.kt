package com.amf.amflix.retrofit.models.Crew

data class CrewResponse(
    val crew: List<Crew>
)

data class Crew(
    val name: String,
    val job: String,
    val profile_path: String
)