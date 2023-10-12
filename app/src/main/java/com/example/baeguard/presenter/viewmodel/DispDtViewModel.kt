package com.example.baeguard.presenter.viewmodel

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

val TAG = "BAE DISP DT VIEW MODEL"

@HiltViewModel
class DispDtViewModel @Inject constructor(
    val repository: Repository
): ViewModel() {

    //Dispositivo

    private val _dispositivo = MutableLiveData<UiState<Dispositivo>>()
    val dispositivo: LiveData<UiState<Dispositivo>>
        get() = _dispositivo

    fun getDispositivo(dispositivo: String){
        _dispositivo.value = UiState.Loading
        repository.getDispositivo(dispositivo) { _dispositivo.value = it }
    }

    private val _updateDispositivo = MutableLiveData<UiState<String>>()
    val updateDispositivo: LiveData<UiState<String>>
        get() = _updateDispositivo

    fun updateDispositivo(dispositivo: Dispositivo){
        _updateDispositivo.value = UiState.Loading
        repository.updateDispositivo(dispositivo){ _updateDispositivo.value = it }
    }

    private val _deleteDispositivo = MutableLiveData<UiState<String>>()
    val deleteDispositivo: LiveData<UiState<String>>
        get() = _deleteDispositivo

    fun deleteDispositivo(dispositivo: Dispositivo){
        _deleteDispositivo.value = UiState.Loading
        repository.deleteDispositivo(dispositivo){ _deleteDispositivo.value = it }
    }

    //Ambientes

    private val _allAmbientes = MutableLiveData<UiState<List<Ambiente>>>()
    val allambientes: LiveData<UiState<List<Ambiente>>>
        get() = _allAmbientes

    fun getAllAmbientes(){
        _allAmbientes.value = UiState.Loading
        repository.getAllAmbiente{ _allAmbientes.value = it }
    }

    private val _ambiente = MutableLiveData<UiState<Ambiente>>()
    val ambiente: LiveData<UiState<Ambiente>>
        get() = _ambiente

    fun getAmbiente(ambiente: DocumentReference){
        _ambiente.value = UiState.Loading
        repository.getAmbiente(ambiente) { result ->
            when (result) {
                is UiState.Loading -> {

                }
                is UiState.Failure -> {
                    Log.e(com.example.baeguard.viewmodel.TAG, result.error.toString())
                }
                is UiState.Success -> {
                    _ambiente.value = result
                }
            }
        }
    }

    private val _addAmbiente = MutableLiveData<UiState<String>>()
    val addAmbiente: LiveData<UiState<String>>
        get() = _addAmbiente

    fun addAmbiente(ambiente: Ambiente): String{
        _addAmbiente.value = UiState.Loading
        return repository.addAmbiente(ambiente){ _addAmbiente.value = it }
    }

}