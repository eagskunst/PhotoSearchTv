package com.eagskunst.photosearch.presentation.adapter

import android.content.Context
import com.eagskunst.photosearch.R

class NoMorePhotosAdapter(private val context: Context) : BaseTextAdapter() {

    var searchTerm = ""
    var isEmpty = true

    override val textStringRes: CharSequence
        get() = if (isEmpty) {
            context.getString(R.string.label_no_photo_found, searchTerm)
        } else {
            context.getString(R.string.label_no_more_photos_found, searchTerm)
        }
}