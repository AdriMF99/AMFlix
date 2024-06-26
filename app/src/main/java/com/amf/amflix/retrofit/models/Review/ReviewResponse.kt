package com.amf.amflix.retrofit.models.Review

data class ReviewResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
)