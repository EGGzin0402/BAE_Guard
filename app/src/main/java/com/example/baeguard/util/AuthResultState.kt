package com.example.baeguard.util

sealed class AuthResultState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : AuthResultState<T>(data)
    class Error<T>(message: String, data: T? = null) : AuthResultState<T>(data, message)
    class Loading<T>(data: T? = null) : AuthResultState<T>(data)
}