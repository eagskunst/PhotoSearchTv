package com.eagskunst.photosearch.data.service

import com.eagskunst.photosearch.data.response.FeedResponse
import com.eagskunst.photosearch.data.response.PhotoInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

    companion object {
        const val ITEMS_PER_AGE = 10
    }

    @GET(".")
    suspend fun getPublicFeedImages(
        @Query("page") page: Int,
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("per_page") itemsPerPage: Int = ITEMS_PER_AGE,
    ): FeedResponse

    @GET(".")
    suspend fun searchImages(
        @Query("text", encoded = true) searchText: String,
        @Query("page") page: Int,
        @Query("method") method: String = "flickr.photos.search",
        @Query("per_page") itemsPerPage: Int = ITEMS_PER_AGE,
    ): FeedResponse

    @GET(".")
    suspend fun getPhotoInfo(
        @Query("photo_id") photoId: String,
        @Query("secret") secret: String,
        @Query("method") method: String = "flickr.photos.getInfo",
    ): PhotoInfoResponse
}