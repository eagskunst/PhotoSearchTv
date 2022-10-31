package com.eagskunst.photosearch.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoPaginationInfoEntity(
    val maxPage: Int,
    val currentPage: Int,
    val photos: List<PhotoEntity>
) : Parcelable
