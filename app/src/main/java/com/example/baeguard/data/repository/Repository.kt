package com.example.baeguard.data.repository

import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.DocumentReference

/*
Interface usada para se comunicar com o repositório do Firebase
*/

interface Repository {

    //Dispositivo
    fun getAllDispositivos(result: (UiState<List<Dispositivo>>) -> Unit)
    fun getDispositivo(dispositivo: String, result: (UiState<Dispositivo>) -> Unit)
    fun addDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit)
    fun updateDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit)
    fun deleteDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit)

    //Ambiente
    fun getAllAmbiente(result: (UiState<List<Ambiente>>) -> Unit)
    fun getAmbiente(ambiente: DocumentReference, result: (UiState<Ambiente>) -> Unit)
    fun addAmbiente(ambiente: Ambiente, result: (UiState<String>) -> Unit): String
    fun updateAmbiente(ambiente: Ambiente, result: (UiState<String>) -> Unit)
    fun deleteAmbiente(/*TODO*/)
}