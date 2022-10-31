package com.eagskunst.photosearch.commons

import com.eagskunst.photosearch.data.response.PhotoInfo
import com.eagskunst.photosearch.domain.entity.PhotoEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PhotoInfoMapper @Inject constructor() : Mapper<PhotoInfo, PhotoEntity> {

    private val sdf = SimpleDateFormat("MMM d yyyy", Locale.US)

    override suspend fun map(value: PhotoInfo): PhotoEntity {
        val uploadTimestamp = value.dateUploaded.toLong()
        return PhotoEntity(
            id = value.id,
            url = buildUrl(value),
            title = value.title.content.ifEmpty { "Photo" },
            creatorName = value.owner.username,
            dateUploadedTimestamp = uploadTimestamp,
            dateFormatted = formatDate(uploadTimestamp)
        )
    }

    private fun formatDate(uploadTimestamp: Long): String {
        val date = Date(uploadTimestamp * 1000)
        return sdf.format(date)
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