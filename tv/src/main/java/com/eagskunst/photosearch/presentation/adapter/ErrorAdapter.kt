package com.eagskunst.photosearch.presentation.adapter

import android.content.Context
import com.eagskunst.photosearch.R

class ErrorAdapter(private val context: Context) : BaseTextAdapter() {
    override val textStringRes: CharSequence
        get() = context.getString(R.string.label_generic_error)
}