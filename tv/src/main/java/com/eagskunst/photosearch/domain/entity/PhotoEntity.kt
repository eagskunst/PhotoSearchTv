package com.eagskunst.photosearch.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoEntity(
    val id: String,
    val url: String,
    val title: String,
    val creatorName: String,
    val dateUploadedTimestamp: Long,
    val dateFormatted: String
) : Parcelable {
    fun creatorNameWithDate(separator: Char = '/') = "$creatorName $separator $dateFormatted"
}
