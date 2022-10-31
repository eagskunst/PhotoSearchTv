package com.eagskunst.photosearch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.commons.exception.EmptyPhotosException
import com.eagskunst.photosearch.domain.entity.PhotoPaginationInfoEntity
import com.eagskunst.photosearch.domain.interactor.GetPhotosFromFeed
import com.eagskunst.photosearch.domain.interactor.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhotosFromFeed: GetPhotosFromFeed,
    private val searchPhotos: SearchPhotos,
) : ViewModel() {

    var searchTerm: String = ""
    private val _photos = MutableLiveData<MainViewState>(MainViewState.Loading)
    val photos: LiveData<MainViewState>
        get() = _photos
    var isFromFeed = true

    fun obtainPhotosFromFeed(page: Int) {
        val currentState = getCurrentStateAndChangeToLoading(searchTerm)
        viewModelScope.launch {
            val photosResult = getPhotosFromFeed(page)
            handlePhotosResult(photosResult, currentState, searchTerm)
        }
    }

    fun searchPhotosOf(text: String, page: Int) {
        isFromFeed = false
        val currentPage = obtainCurrentPage(_photos.value ?: MainViewState.Loading)
        if (text == searchTerm && page == currentPage) {
            return
        }

        val currentState = getCurrentStateAndChangeToLoading(text)
        searchTerm = text
        viewModelScope.launch {
            val photosResult = searchPhotos(text, page)
            val titleText = "Search results for \"$searchTerm\""
            handlePhotosResult(photosResult, currentState, titleText)
        }
    }

    private fun obtainCurrentPage(currentState: MainViewState): Int {
        return if (currentState is MainViewState.Photos) currentState.page else 1
    }

    private fun handlePhotosResult(
        photosResult: DataResult<PhotoPaginationInfoEntity>,
        currentState: MainViewState?,
        titleText: String
    ) {
        if (photosResult is ErrorResult) {
            if (currentState is MainViewState.Photos) {
                _photos.value = MainViewState.NoMorePhotos(
                    photoList = currentState.photoList,
                    page = currentState.page,
                    text = currentState.text,
                    maxPage = currentState.page
                )
            } else if (photosResult.throwable is EmptyPhotosException) {
                _photos.value = MainViewState.NoMorePhotos(
                    photoList = emptyList(),
                    page = 0,
                    text = titleText,
                    maxPage = 0
                )
            } else {
                _photos.value = MainViewState.GeneralError
            }
            return
        }
        //we know it must be success
        val photosPaginationInfo = photosResult.getOrThrow()
        if (currentState is MainViewState.Loading) {
            _photos.value = MainViewState.Photos(
                photoList = photosPaginationInfo.photos,
                page = photosPaginationInfo.currentPage,
                text = titleText,
                maxPage = photosPaginationInfo.maxPage
            )
        } else if (currentState is MainViewState.Photos) {
            _photos.value = MainViewState.Photos(
                photoList = currentState.photoList + photosPaginationInfo.photos,
                page = photosPaginationInfo.currentPage,
                text = titleText,
                maxPage = photosPaginationInfo.maxPage
            )
        }
    }

    private fun getCurrentStateAndChangeToLoading(text: String): MainViewState {
        viewModelScope.coroutineContext.cancelChildren()
        var currentState = _photos.value ?: MainViewState.Loading
        if (text != searchTerm) {
            _photos.value = MainViewState.Loading
            currentState = MainViewState.Loading
        } else if (currentState is MainViewState.Photos) {
            _photos.value = MainViewState.LoadingMore(currentState)
        } else {
            _photos.value = MainViewState.Loading
            currentState = MainViewState.Loading
        }
        return currentState
    }

    fun paginate() {
        val currentState = _photos.value
        Timber.d("Current state: $currentState")
        if (currentState is MainViewState.Loading ||
            currentState is MainViewState.LoadingMore ||
            currentState !is MainViewState.Photos
        ) {
            return
        }
        val currentPage = currentState.page
        if (isFromFeed) {
            obtainPhotosFromFeed(currentPage + 1)
        } else {
            searchPhotosOf(searchTerm, currentPage + 1)
        }
        return
    }

    fun getPhotosState(): PhotoPaginationInfoEntity? {
        when (val currentState = _photos.value) {
            is MainViewState.Photos -> return PhotoPaginationInfoEntity(
                0,
                0,
                currentState.photoList
            )
            is MainViewState.LoadingMore -> return PhotoPaginationInfoEntity(
                0,
                0,
                currentState.photos.photoList
            )
            is MainViewState.NoMorePhotos -> return PhotoPaginationInfoEntity(
                0,
                0,
                currentState.photoList
            )
            else -> return null
        }
    }
}