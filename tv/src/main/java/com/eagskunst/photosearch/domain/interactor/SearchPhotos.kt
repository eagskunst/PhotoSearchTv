package com.eagskunst.photosearch.domain.interactor

import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.thread.CoroutineDispatchers
import com.eagskunst.photosearch.data.datasource.FlickrImageDataSource
import com.eagskunst.photosearch.domain.BaseInteractor
import com.eagskunst.photosearch.domain.entity.PhotoPaginationInfoEntity
import javax.inject.Inject

class SearchPhotos @Inject constructor(
    private val dataSource: FlickrImageDataSource,
    dispatchers: CoroutineDispatchers,
) : BaseInteractor(dispatchers) {

    suspend operator fun invoke(text: String, page: Int): DataResult<PhotoPaginationInfoEntity> {
        return switchToIoWithResult {
            dataSource.searchPhotosFeedOf(
                text = text, page = page
            )
        }
    }
}