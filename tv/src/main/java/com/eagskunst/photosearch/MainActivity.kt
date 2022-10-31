package com.eagskunst.photosearch

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
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

    companion object {
        const val COLUMNS = 3
    }

    private val viewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val photosAdapter = MainViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupClicks()
        viewModel.titleText = getString(R.string.title_main_screen_initial)
        viewModel.photos.observe(this) { state ->
            Timber.d("Photos result: $state")
            if (state is MainViewState.Photos) {
                photosAdapter.photoList = state.photoList
                binding.tvTitle.text = state.text
            }
        }
        viewModel.obtainPhotosFromFeed(1)
    }

    private fun setupRecyclerView() {
        photosAdapter.requestSearchFocus = { binding.btSearch.requestFocus() }
        with(binding.rvPhotos) {
            setNumColumns(COLUMNS)
            setColumnWidth(0)
            adapter = photosAdapter
            isFocusable = true
            isFocusableInTouchMode = true
            setOnFocusChangeListener { _, b ->
                Timber.d("recyclerview focus: $b")
            }
        }
    }

    private fun setupClicks() {
        binding.btSearch.setOnClickListener {
            openKeyboard()
        }
        binding.etSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPhotosOf(binding.etSearchText.text.toString())
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