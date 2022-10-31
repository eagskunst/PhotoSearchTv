package com.eagskunst.photosearch.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eagskunst.photosearch.R
import com.eagskunst.photosearch.commons.inflate
import com.eagskunst.photosearch.commons.viewBinding
import com.eagskunst.photosearch.databinding.ViewHolderPhotoBinding
import com.eagskunst.photosearch.domain.entity.PhotoEntity
import com.eagskunst.photosearch.presentation.MainActivity

class MainViewAdapter : RecyclerView.Adapter<MainViewAdapter.PhotoViewHolder>() {

    var requestSearchFocus: () -> Unit = {}
    var endReachCallback: () -> Unit = {}
    var clickCallback: (position: Int) -> Unit = {}
    var photoList = listOf<PhotoEntity>()
        set(value) {
            val diffCallback = MainViewDiffCallback(field, value)
            val diffPhotos = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffPhotos.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = parent.inflate(R.layout.view_holder_photo).apply {
            isFocusable = true
            isFocusableInTouchMode = true
        }
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        setFocusLogic(holder, position)
        holder.bind(photoList[position])
        holder.itemView.setOnClickListener { clickCallback(position) }
    }

    private fun setFocusLogic(
        holder: PhotoViewHolder,
        position: Int
    ) {
        holder.binding.ivFlickrPhoto.isFocusable = false
        holder.binding.ivFlickrPhoto.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.clearFocus()
                requestSearchFocus()
            }
        }
        if (position < MainActivity.COLUMNS) {
            holder.binding.ivFlickrPhoto.isFocusable = true
            holder.itemView.nextFocusUpId = R.id.ivFlickrPhoto
        }
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            val validNotifyLoadMorePosition =
                listOf(photoList.lastIndex - 2, photoList.lastIndex - 1, photoList.lastIndex)
            val isValidLoadMorePos = position in validNotifyLoadMorePosition
            if (isValidLoadMorePos && hasFocus) {
                endReachCallback()
            }
        }
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding by viewBinding(ViewHolderPhotoBinding::bind)

        fun bind(photoEntity: PhotoEntity) {
            with(photoEntity) {
                binding.ivFlickrPhoto.load(url) {
                    placeholder(R.drawable.image_placeholder)
                }
                binding.tvPhotoTitle.text = title
                binding.tvUsernameDate.text = creatorNameWithDate()
            }
        }
    }
}