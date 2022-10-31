package com.eagskunst.photosearch.domain.entity

data class PhotoEntity(
    val id: String,
    val url: String,
    val title: String,
    val creatorName: String,
    val dateUploadedTimestamp: Long,
    val dateFormatted: String
) {
    fun creatorNameWithDate(separator: Char = '/') = "$creatorName $separator $dateFormatted"
}
