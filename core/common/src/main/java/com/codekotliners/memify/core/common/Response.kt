package com.codekotliners.memify.core.common

sealed class Response<out T> {
    data object Loading : Response<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : Response<T>()

    data class Failure<out T>(
        val error: Exception,
    ) : Response<T>()
}
