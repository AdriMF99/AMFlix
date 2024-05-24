package com.amf.amflix.ui.Search

import com.amf.amflix.retrofit.models.movies.Movie
import com.amf.amflix.retrofit.models.series.TVSeries

data class SearchResponse(
    val results: List<Movie>
)

data class SearchResponsetv(
    val results: List<TVSeries>
)
