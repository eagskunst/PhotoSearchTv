package com.eagskunst.photosearch

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.eagskunst.photosearch.commons.viewBinding
import com.eagskunst.photosearch.databinding.ActivityMainBinding
import com.eagskunst.photosearch.presentation.MainViewModel
import com.eagskunst.photosearch.presentation.MainViewState
import com.eagskunst.photosearch.presentation.adapter.ErrorAdapter
import com.eagskunst.photosearch.presentation.adapter.LoadingAdapter
import com.eagskunst.photosearch.presentation.adapter.MainViewAdapter
import com.eagskunst.photosearch.presentation.adapter.NoMorePhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    companion object {
        const val COLUMNS = 3
    }

    private val viewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val allAdapters = ConcatAdapter()
    private val loadingAdapter = LoadingAdapter()
    private val photosAdapter = MainViewAdapter()
    private val noMorePhotosAdapter = NoMorePhotosAdapter(this)
    private val errorAdapter = ErrorAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupClicks()
        viewModel.searchTerm = getString(R.string.title_main_screen_initial)
        viewModel.photos.observe(this) { state ->
            Timber.d("Photos result: $state")
            binding.rvPhotos.post {
                handleState(state)
            }
        }
        viewModel.obtainPhotosFromFeed(page = 1)
    }

    private fun handleState(state: MainViewState?) {
        if (state == null) {
            return
        }
        when (state) {
            MainViewState.GeneralError -> {
                allAdapters.removeAdapter(loadingAdapter)
                allAdapters.addAdapter(errorAdapter)
            }
            MainViewState.Loading -> {
                noMorePhotosAdapter.isEmpty = true
                allAdapters.removeAdapter(photosAdapter)
                allAdapters.removeAdapter(loadingAdapter)
                allAdapters.removeAdapter(noMorePhotosAdapter)
                allAdapters.addAdapter(loadingAdapter)
            }
            is MainViewState.LoadingMore -> {
                allAdapters.addAdapter(loadingAdapter)
            }
            is MainViewState.NoMorePhotos -> {
                allAdapters.removeAdapter(loadingAdapter)
                allAdapters.addAdapter(noMorePhotosAdapter)
            }
            is MainViewState.Photos -> {
                noMorePhotosAdapter.isEmpty = false
                photosAdapter.photoList = state.photoList
                binding.tvTitle.text = state.text
                allAdapters.removeAdapter(loadingAdapter)
                allAdapters.addAdapter(photosAdapter)
            }
        }
    }

    private fun setupRecyclerView() {
        photosAdapter.requestSearchFocus = { binding.btSearch.requestFocus() }
        photosAdapter.endReachCallback = { viewModel.paginate() }
        with(binding.rvPhotos) {
            setNumColumns(COLUMNS)
            setColumnWidth(0)
            adapter = allAdapters
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }

    private fun setupClicks() {
        binding.btSearch.setOnClickListener {
            openKeyboard()
        }
        binding.etSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPhotosOf(
                    text = binding.etSearchText.text.toString(),
                    page = 1
                )
                binding.etSearchText.clearFocus()
                binding.ilSearch.isVisible = false
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun openKeyboard() {
        binding.ilSearch.isVisible = true
        if (binding.etSearchText.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.btSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}