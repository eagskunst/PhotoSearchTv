package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json


data class PhotoInfo(
    @Json(name = "dateuploaded")
    val dateUploaded: String,
    @Json(name = "farm")
    val farm: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "isfavorite")
    val isFavorite: Int,
    @Json(name = "license")
    val license: String,
    @Json(name = "media")
    val media: String,
    @Json(name = "owner")
    val owner: Owner,
    @Json(name = "rotation")
    val rotation: Int,
    @Json(name = "safety_level")
    val safetyLevel: String,
    @Json(name = "secret")
    val secret: String,
    @Json(name = "server")
    val server: String,
    @Json(name = "title")
    val title: Title,
)