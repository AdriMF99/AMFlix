package com.amf.amflix.retrofit.models.series

import com.amf.amflix.retrofit.models.movies.Genre

data class TVSeries(
    val backdrop_path: String,
    val first_air_date: String,
    val genres: List<Genre>?,
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    val tagline: String?
)

data class Genre(
    val id: Int,
    val name: String
)