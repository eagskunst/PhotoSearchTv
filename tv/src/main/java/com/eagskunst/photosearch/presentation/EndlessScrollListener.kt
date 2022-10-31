package com.eagskunst.photosearch.presentation

import androidx.recyclerview.widget.RecyclerView


class EndlessScrollListener(
    private val layoutManager: RecyclerView.LayoutManager?,
    private val onLoadMore: (Int) -> Boolean
) : RecyclerView.OnScrollListener() {

    // The minimum number of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 5

    // The current offset index of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private var startingPageIndex = 0

}