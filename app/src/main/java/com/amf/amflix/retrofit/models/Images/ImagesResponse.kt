package com.amf.amflix.retrofit.models.Images

data class ImagesResponse(
    val posters: List<Image>
)

data class Image(
    val file_path: String
)