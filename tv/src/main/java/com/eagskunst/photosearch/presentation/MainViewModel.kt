package com.eagskunst.photosearch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.domain.interactor.GetPhotosFromFeed
import com.eagskunst.photosearch.domain.interactor.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhotosFromFeed: GetPhotosFromFeed,
    private val searchPhotos: SearchPhotos,
) : ViewModel() {

    private val _photos = MutableLiveData<MainViewState>(MainViewState.Loading)
    val photos: LiveData<MainViewState>
        get() = _photos

    fun obtainPhotosFromFeed(page: Int) {
        val currentState = _photos.value
        if (currentState is MainViewState.Photos) {
            _photos.value = MainViewState.LoadingMore(currentState)
        }
        viewModelScope.launch {
            val photosResult = getPhotosFromFeed(page)
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
            }
            val photosPaginationInfo = photosResult.getOrThrow()
            if (currentState is MainViewState.Loading) {
                _photos.value = MainViewState.Photos(
                    photoList = photosPaginationInfo.photos,
                    page = photosPaginationInfo.currentPage,
                    text = ""
                )
            } else if (currentState is MainViewState.Photos) {
                _photos.value = MainViewState.Photos(
                    photoList = currentState.photoList + photosPaginationInfo.photos,
                    page = photosPaginationInfo.currentPage,
                    text = ""
                )
            }
        }
    }
}