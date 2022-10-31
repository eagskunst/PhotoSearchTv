package com.eagskunst.photosearch.commons

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> FragmentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

inline fun <T : ViewBinding> RecyclerView.ViewHolder.viewBinding(crossinline viewBindingFactory: (View) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        viewBindingFactory(itemView)
    }