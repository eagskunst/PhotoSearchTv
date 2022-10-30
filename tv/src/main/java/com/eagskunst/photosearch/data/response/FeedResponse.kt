package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json

data class FeedResponse(
    @Json(name = "photos")
    val photos: Photos,
    @Json(name = "stat")
    val stat: String
)