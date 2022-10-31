package com.eagskunst.photosearch.presentation.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.eagskunst.photosearch.R
import com.eagskunst.photosearch.commons.inflate
import com.eagskunst.photosearch.databinding.ViewHolderTextBinding

abstract class BaseTextAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val textStringRes: CharSequence

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.view_holder_text)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = ViewHolderTextBinding.bind(holder.itemView)
        binding.root.isVisible = position == 1
        binding.tvGeneric.text = textStringRes
    }

    override fun getItemCount(): Int {
        return 2
    }

}