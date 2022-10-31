package com.eagskunst.photosearch.presentation

import androidx.recyclerview.widget.DiffUtil
import com.eagskunst.photosearch.domain.entity.PhotoEntity

class MainViewDiffCallback(
    private val oldList: List<PhotoEntity>,
    private val newList: List<PhotoEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}