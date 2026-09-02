package com.vocabplus.app.core.util

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val message: String? = exception.localizedMessage) : Result<Nothing>
    data object Loading : Result<Nothing>

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}