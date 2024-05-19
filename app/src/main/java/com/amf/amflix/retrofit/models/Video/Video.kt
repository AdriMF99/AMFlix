package com.amf.amflix.retrofit.models.Video

data class Video(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean,
    val published_at: String
)