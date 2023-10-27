package com.example.baeguard.presenter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.baeguard.data.model.SignInResult
import com.example.baeguard.data.repository.AuthRepository
import com.example.baeguard.util.GoogleSignInState
import com.example.baeguard.util.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    //Google

    private val _state = MutableStateFlow(GoogleSignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState(){
        _state.update { GoogleSignInState() }
    }

    //Email e Senha

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    suspend fun loginUser(email: String, password: String){
        var result = authRepository.loginUser(email, password)
        _signInState.update { it.copy(
            isSuccess = result.data != null,
            isError = result.errorMessage
        ) }
    }

    fun resetSignInState(){
        _signInState.update { SignInState() }
    }

}