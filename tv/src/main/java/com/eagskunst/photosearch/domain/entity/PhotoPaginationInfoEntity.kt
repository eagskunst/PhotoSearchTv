package com.eagskunst.photosearch.domain.entity

data class PhotoPaginationInfoEntity(
    val maxPage: Int,
    val currentPage: Int,
    val photos: List<PhotoEntity>
) {
    companion object {
        val EMPTY = PhotoPaginationInfoEntity(0, 0, emptyList())
    }
}
