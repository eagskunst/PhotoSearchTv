package com.eagskunst.photosearch.domain.entity

data class PhotoEntity(
    val url: String,
    val title: String,
    val creatorName: String,
    val dateUploadedTimestamp: Long
)
