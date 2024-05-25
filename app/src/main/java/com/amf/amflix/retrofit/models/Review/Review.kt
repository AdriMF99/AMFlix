package com.amf.amflix.retrofit.models.Review

data class Review(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val id: String,
    val url: String
)

data class AuthorDetails(
    val name: String,
    val username: String,
    val avatar_path: String?,
    val rating: Float?
)