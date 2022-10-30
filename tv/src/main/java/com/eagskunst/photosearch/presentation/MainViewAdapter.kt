package com.eagskunst.photosearch.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eagskunst.photosearch.R
import com.eagskunst.photosearch.commons.viewBinding
import com.eagskunst.photosearch.databinding.ViewHolderPhotoBinding
import com.eagskunst.photosearch.domain.entity.PhotoEntity

class MainViewAdapter : RecyclerView.Adapter<MainViewAdapter.PhotoViewHolder>() {

    var photoList = listOf<PhotoEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_photo, parent, false).also {
                it.isFocusable = true
                it.isFocusableInTouchMode = true
            }
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding by viewBinding(ViewHolderPhotoBinding::bind)

        fun bind(photoEntity: PhotoEntity) {
            with(photoEntity) {
                binding.ivFlickrPhoto.load(url) {
                    //todo add placeholder and error
                }
                binding.tvPhotoTitle.text = title
                binding.tvUsernameDate.text = creatorNameWithDate()
            }
        }
    }
}