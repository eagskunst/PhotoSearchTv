package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json

data class Photos(
    @Json(name = "page")
    val page: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "perpage")
    val perPage: Int,
    @Json(name = "photo")
    val photoResponse: List<Photo>,
    @Json(name = "total")
    val total: Int
)