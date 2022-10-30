package com.eagskunst.photosearch.commons

import com.eagskunst.photosearch.data.response.PhotoInfo
import com.eagskunst.photosearch.domain.entity.PhotoEntity
import javax.inject.Inject

class PhotoInfoMapper @Inject constructor() : Mapper<PhotoInfo, PhotoEntity> {
    override suspend fun map(value: PhotoInfo): PhotoEntity {
        return PhotoEntity(
            url = buildUrl(value),
            title = value.title.content.ifEmpty { "Photo" },
            creatorName = value.owner.username,
            dateUploadedTimestamp = value.dateUploaded.toLong()
        )
    }

    private fun buildUrl(value: PhotoInfo): String {
        val builder = StringBuilder("https://live.staticflickr.com/")
        builder.append(value.server)
        builder.append("/${value.id}_")
        builder.append(value.secret)
        builder.append(".jpg")
        return builder.toString()
    }
}