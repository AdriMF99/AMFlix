package com.amf.amflix.retrofit.models.Cast

data class CastResponse(
    val cast: List<Cast>
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profile_path: String
)