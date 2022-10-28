package com.eagskunst.photosearch.domain

import com.bumptech.glide.load.HttpException
import com.eagskunst.photosearch.commons.DataResult
import com.eagskunst.photosearch.commons.ErrorMessage
import com.eagskunst.photosearch.commons.ErrorResult
import com.eagskunst.photosearch.commons.thread.Asyncable
import com.eagskunst.photosearch.commons.thread.CoroutineDispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Created by eagskunst in 25/7/2020.
 */

abstract class BaseInteractor(protected val internalDispatchers: CoroutineDispatchers) : Asyncable {

    protected suspend inline fun switchToIo(crossinline block: suspend () -> Unit) {
        withContext(internalDispatchers.io) { block() }
    }

    protected suspend inline fun <T> switchToIoWithResult(crossinline block: suspend () -> T): T {
        return withContext(internalDispatchers.io) { block() }
    }

    protected fun <T> addErrorInformationToResult(errorResult: ErrorResult<T>): DataResult<T> {
        val errorInfo = errorResult.errorInfo
        val newInfo = when (errorInfo.throwable) {
            is HttpException -> errorInfo.copy(message = ErrorMessage.Unknown)
            is SocketTimeoutException -> errorInfo.copy(message = ErrorMessage.Timeout)
            is IOException -> errorInfo.copy(message = ErrorMessage.Connection)
            else -> errorInfo.copy(message = ErrorMessage.Unknown)
        }

        return ErrorResult(errorResult.throwable, newInfo)
    }

    companion object {
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }
}