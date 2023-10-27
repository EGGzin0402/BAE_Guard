package com.example.baeguard.presenter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.baeguard.data.repository.AuthRepository
import com.example.baeguard.util.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    //Registro de Email e Senha

    private val _signUpState = MutableStateFlow(SignInState())
    val signUpState = _signUpState.asStateFlow()

    suspend fun registerUser(email: String, password: String){
        var result = authRepository.registerUser(email, password)
        _signUpState.update { it.copy(
            isSuccess = result.data != null,
            isError = result.errorMessage
        ) }
    }

    fun resetSignInState(){
        _signUpState.update { SignInState() }
    }

}