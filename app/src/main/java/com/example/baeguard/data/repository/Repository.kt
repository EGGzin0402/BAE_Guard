package com.example.baeguard.data.repository

import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.data.model.Historico
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.DocumentReference

/*
Interface usada para se comunicar com o repositório do Firebase
*/

interface Repository {

    //Dispositivo
    fun getAllDispositivos(user: UserData?, result: (UiState<List<Dispositivo>>) -> Unit)
    fun getDispositivo(user: UserData?, dispositivo: String, result: (UiState<Dispositivo>) -> Unit)
    fun addDispositivo(user: UserData?, result: (UiState<String>) -> Unit)
    fun updateDispositivo(user: UserData?, dispositivo: Dispositivo, result: (UiState<String>) -> Unit)
    fun deleteDispositivo(user: UserData?, dispositivo: Dispositivo, result: (UiState<String>) -> Unit)

    //Ambiente
    fun getAllAmbiente(user: UserData?, result: (UiState<List<Ambiente>>) -> Unit)
    fun getAmbiente(user: UserData?, ambiente: DocumentReference, result: (UiState<Ambiente>) -> Unit)
    fun addAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit): String
    fun updateAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit)
    fun deleteAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit)

    //Histórico
    fun getAllHistorico(user: UserData?, result: (UiState<List<Historico>>) -> Unit)
}