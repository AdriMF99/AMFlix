package com.amf.amflix.retrofit.models.movies

data class Movie(

    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<Genre>?,
    val id: Int,
    val original_language: String,
    val original_title: String?,
    val original_name: String?,
    val name: String?,
    val media_type: String?,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String?
)