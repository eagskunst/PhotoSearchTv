package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json


data class Title(
    @Json(name = "_content")
    val content: String
)