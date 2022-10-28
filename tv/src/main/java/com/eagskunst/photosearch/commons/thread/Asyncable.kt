package com.eagskunst.photosearch.commons.thread

import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.commons.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

interface Asyncable {

    suspend fun <T> runSafely(block: suspend () -> T): DataResult<T> {
        return try {
            val res = block()
            Success(res)
        } catch (e: Exception) {
            ErrorResult(e.cause ?: Exception(""))
        }
    }

    suspend fun runAndForget(block: suspend () -> Unit) = coroutineScope {
        launch { block() }
    }

    suspend fun runDeferred(block: suspend () -> Unit) = coroutineScope {
        async { block }
    }
}
