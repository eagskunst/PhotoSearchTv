package com.eagskunst.photosearch.commons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes


fun ViewGroup.inflate(@LayoutRes layout: Int): View = LayoutInflater.from(context)
    .inflate(layout, this, false)