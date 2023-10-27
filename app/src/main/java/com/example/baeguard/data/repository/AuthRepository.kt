package com.example.baeguard.data.repository

import com.example.baeguard.data.model.SignInResult

interface AuthRepository {

    suspend fun loginUser(email: String, password: String): SignInResult
    suspend fun registerUser(email: String, password: String): SignInResult

}