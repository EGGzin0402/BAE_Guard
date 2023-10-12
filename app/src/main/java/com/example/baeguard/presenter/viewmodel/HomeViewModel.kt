package com.example.baeguard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.data.repository.Repository
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

val TAG = "BAE HOME VIEW MODEL"

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    //Dispositivo

    private val _allDispositivos = MutableLiveData<UiState<List<Dispositivo>>>()
    val alldispositivos: LiveData<UiState<List<Dispositivo>>>
        get() = _allDispositivos

    fun getAllDispositivos(){
        _allDispositivos.value = UiState.Loading
        repository.getAllDispositivos { _allDispositivos.value = it }
    }

    private val _addDispositivo = MutableLiveData<UiState<String>>()
    val addDispostivo: LiveData<UiState<String>>
        get() = _addDispositivo

    fun addDispositivo(dispositivo: Dispositivo){
        _addDispositivo.value = UiState.Loading
        repository.addDispositivo(dispositivo){ _addDispositivo.value = it }
    }


    //Ambiente

    private val _ambiente = MutableLiveData<UiState<Ambiente>>()
    val ambiente: LiveData<UiState<Ambiente>>
        get() = _ambiente

    private val ambienteTitles = mutableMapOf<DocumentReference, String>()

    fun getAmbiente(ambiente:DocumentReference){
        _ambiente.value = UiState.Loading
        repository.getAmbiente(ambiente) { result ->
            when (result) {
                is UiState.Loading -> {

                }
                is UiState.Failure -> {
                    Log.e(TAG, result.error.toString())
                }
                is UiState.Success -> {
                    val amb = result.data
                    ambienteTitles[ambiente] = amb.nome
                }
            }
            _ambiente.value = result
        }
    }

    fun getAmbienteTitle(ambiente: DocumentReference): String {
        return ambienteTitles[ambiente] ?: ""
    }

}