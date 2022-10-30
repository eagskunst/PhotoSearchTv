package com.eagskunst.photosearch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.eagskunst.photosearch.commons.viewBinding
import com.eagskunst.photosearch.databinding.ActivityMainBinding
import com.eagskunst.photosearch.presentation.MainViewAdapter
import com.eagskunst.photosearch.presentation.MainViewModel
import com.eagskunst.photosearch.presentation.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val photosAdapter = MainViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.photos.observe(this) { state ->
            Timber.d("Photos result: $state")
            if (state is MainViewState.Photos) {
                photosAdapter.photoList = state.photoList
            }
        }
        with(binding.rvPhotos) {
            setNumColumns(3)
            setColumnWidth(0)
            adapter = photosAdapter
        }
        viewModel.obtainPhotosFromFeed(1)
    }
}