package com.amf.amflix.retrofit.models.series

data class TopTVSeriesResponse(
    val page: Int,
    val results: List<TVSeries>,
    val total_pages: Int,
    val total_results: Int
)