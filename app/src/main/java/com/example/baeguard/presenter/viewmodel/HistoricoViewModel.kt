package com.example.baeguard.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baeguard.data.model.Historico
import com.example.baeguard.data.repository.GoogleAuthUiClient
import com.example.baeguard.data.repository.Repository
import com.example.baeguard.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoricoViewModel @Inject constructor(
    val repository: Repository,
    val googleAuthUiClient: GoogleAuthUiClient
): ViewModel() {

    //Historico

    private val _allHistorico = MutableLiveData<UiState<List<Historico>>>()
    val allhistorico: LiveData<UiState<List<Historico>>>
        get() = _allHistorico

    fun getAllHistorico(){
        _allHistorico.value = UiState.Loading
        repository.getAllHistorico(googleAuthUiClient.getSignedInUser()){ _allHistorico.value = it }
    }

}