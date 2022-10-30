package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json


data class PhotoInfoResponse(
    @Json(name = "photo")
    val photo: PhotoInfo,
    @Json(name = "stat")
    val stat: String
)