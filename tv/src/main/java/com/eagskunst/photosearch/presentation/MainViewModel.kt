package com.eagskunst.photosearch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.domain.entity.PhotoPaginationInfoEntity
import com.eagskunst.photosearch.domain.interactor.GetPhotosFromFeed
import com.eagskunst.photosearch.domain.interactor.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhotosFromFeed: GetPhotosFromFeed,
    private val searchPhotos: SearchPhotos,
) : ViewModel() {

    var titleText: String = ""
    private val _photos = MutableLiveData<MainViewState>(MainViewState.Loading)
    val photos: LiveData<MainViewState>
        get() = _photos

    fun obtainPhotosFromFeed(page: Int) {
        val currentState = changeStateToLoading(titleText)
        viewModelScope.launch {
            val photosResult = getPhotosFromFeed(page)
            handlePhotosResult(photosResult, currentState)
        }
    }

    fun searchPhotosOf(text: String) {
        if (text == titleText) {
            return
        }
        val currentState = changeStateToLoading(text)
        val currentPage = obtainCurrentPage(currentState)
        viewModelScope.launch {
            val photosResult = searchPhotos(text, currentPage)
            titleText = "Search results for \"$text\""
            handlePhotosResult(photosResult, currentState)
        }
    }

    private fun obtainCurrentPage(currentState: MainViewState): Int {
        return if (currentState is MainViewState.Photos) currentState.page else 1
    }

    private fun handlePhotosResult(
        photosResult: DataResult<PhotoPaginationInfoEntity>,
        currentState: MainViewState?
    ) {
        if (photosResult is ErrorResult) {
            if (currentState is MainViewState.Photos) {
                _photos.value = MainViewState.NoMorePhotos(
                    photoList = currentState.photoList,
                    page = currentState.page,
                    text = currentState.text
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
                text = titleText
            )
        } else if (currentState is MainViewState.Photos) {
            _photos.value = MainViewState.Photos(
                photoList = currentState.photoList + photosPaginationInfo.photos,
                page = photosPaginationInfo.currentPage,
                text = titleText
            )
        }
    }

    private fun changeStateToLoading(text: String): MainViewState {
        viewModelScope.coroutineContext.cancelChildren()
        var currentState = _photos.value ?: MainViewState.Loading
        if (text != titleText) {
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
}