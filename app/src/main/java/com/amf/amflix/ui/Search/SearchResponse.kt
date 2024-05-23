package com.amf.amflix.ui.Search

import com.amf.amflix.retrofit.models.movies.Movie

data class SearchResponse(
    val results: List<Movie>
)
