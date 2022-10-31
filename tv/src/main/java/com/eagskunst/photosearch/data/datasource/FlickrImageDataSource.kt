package com.eagskunst.photosearch.data.datasource

import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.commons.PhotoInfoMapper
import com.eagskunst.photosearch.commons.Success
import com.eagskunst.photosearch.commons.exception.EmptyPhotosException
import com.eagskunst.photosearch.commons.thread.Asyncable
import com.eagskunst.photosearch.data.response.FeedResponse
import com.eagskunst.photosearch.data.response.PhotoInfoResponse
import com.eagskunst.photosearch.data.service.ImageService
import com.eagskunst.photosearch.domain.entity.PhotoPaginationInfoEntity
import kotlinx.coroutines.awaitAll
import timber.log.Timber
import javax.inject.Inject

class FlickrImageDataSource @Inject constructor(
    private val imageService: ImageService,
    private val mapper: PhotoInfoMapper,
) : Asyncable {
    private suspend fun getPhotoInfoAsync(photoId: String, secret: String) = runDeferred {
        imageService.getPhotoInfo(photoId, secret)
    }

    suspend fun getPhotosFeedOf(page: Int): DataResult<PhotoPaginationInfoEntity> {
        Timber.d("Searching photos of feed for page $page")
        return when (val photoFeed = runSafely { imageService.getPublicFeedImages(page) }) {
            is ErrorResult -> ErrorResult(photoFeed.throwable, photoFeed.errorInfo)
            is Success -> obtainPhotoInfo(photoFeed)
        }
    }

    suspend fun searchPhotosFeedOf(text: String, page: Int): DataResult<PhotoPaginationInfoEntity> {
        Timber.d("Searching photos of $text for page #$page")
        return when (val photoFeed = runSafely { imageService.searchImages(text, page) }) {
            is ErrorResult -> ErrorResult(photoFeed.throwable, photoFeed.errorInfo)
            is Success -> obtainPhotoInfo(photoFeed)
        }
    }

    private suspend fun obtainPhotoInfo(photoFeed: Success<FeedResponse>): DataResult<PhotoPaginationInfoEntity> {
        Timber.d("Creating tasks")
        val photos = photoFeed.data.photos
        val tasks = photos.photoResponse.map { photo ->
            getPhotoInfoAsync(photo.id, photo.secret)
        }
        Timber.d("Tasks created")
        val photosInfo = tasks.awaitAll()
            .filterIsInstance<Success<PhotoInfoResponse>>()
        if (photosInfo.isEmpty()) {
            return ErrorResult(EmptyPhotosException())
        }
        val photosEntity = photosInfo.map { photosInfoResult ->
            mapper.map(photosInfoResult.get().photo)
        }
        return Success(
            PhotoPaginationInfoEntity(
                maxPage = photos.pages,
                currentPage = photos.page,
                photos = photosEntity
            )
        )
    }
}