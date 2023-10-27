package com.example.baeguard.util

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = ""
)