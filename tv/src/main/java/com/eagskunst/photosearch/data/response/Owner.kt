package com.eagskunst.photosearch.data.response


import com.squareup.moshi.Json


data class Owner(
    @Json(name = "iconfarm")
    val iconFarm: Int,
    @Json(name = "iconserver")
    val iconServer: String,
    @Json(name = "location")
    val location: String?,
    @Json(name = "nsid")
    val nsid: String,
    @Json(name = "path_alias")
    val pathAlias: String?,
    @Json(name = "realname")
    val realName: String,
    @Json(name = "username")
    val username: String
)