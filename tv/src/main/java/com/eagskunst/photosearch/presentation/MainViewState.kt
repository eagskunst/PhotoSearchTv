package com.eagskunst.photosearch.presentation

import com.eagskunst.photosearch.domain.entity.PhotoEntity

sealed class MainViewState {
    object Loading : MainViewState()

    data class Photos(
        val photoList: List<PhotoEntity>,
        val page: Int,
        val text: String = "",
    ) : MainViewState()

    data class NoMorePhotos(
        val photoList: List<PhotoEntity>,
        val page: Int,
        val text: String = "",
    ) : MainViewState()

    object GeneralError : MainViewState()

    data class LoadingMore(val photos: Photos) : MainViewState()
}