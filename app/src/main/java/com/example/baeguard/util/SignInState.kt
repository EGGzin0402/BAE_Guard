package com.example.baeguard.util

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)