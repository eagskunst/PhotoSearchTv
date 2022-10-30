package com.eagskunst.photosearch.di

import com.eagskunst.photosearch.commons.thread.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ThreadModule {
    @Provides
    fun provideDispatchers() = CoroutineDispatchers()
}