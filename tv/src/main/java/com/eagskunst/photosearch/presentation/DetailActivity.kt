package com.eagskunst.photosearch.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import coil.load
import com.eagskunst.photosearch.commons.viewBinding
import com.eagskunst.photosearch.databinding.ActivityDetailBinding
import com.eagskunst.photosearch.domain.entity.PhotoPaginationInfoEntity

class DetailActivity : FragmentActivity() {

    private val binding by viewBinding(ActivityDetailBinding::inflate)

    companion object {
        const val KEY_PAGINATION_ENTITY = "pagination_entity"
        const val KEY_POSITION = "pos"

        fun createIntent(
            context: Context,
            photoPaginationInfoEntity: PhotoPaginationInfoEntity,
            position: Int
        ): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(KEY_PAGINATION_ENTITY, photoPaginationInfoEntity)
                putExtra(KEY_POSITION, position)
            }
        }
    }

    private val photoPaginationInfo: PhotoPaginationInfoEntity?
        get() = intent.extras?.getParcelable(KEY_PAGINATION_ENTITY)

    private val photoListPosition: Int
        get() = intent.extras?.getInt(KEY_POSITION) ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val photoPaginationInfoFinal = photoPaginationInfo ?: return
        binding.ivDetailPhoto.load(photoPaginationInfoFinal.photos[photoListPosition].url)
    }
}