package com.example.baeguard.data.repository

import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.util.FirestoreTables
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

val TAG = "BAE REPOSITORY IMP"

/*
Classe de Implementação da interface do repositório
 */

class RepositoryImp(
    val database: FirebaseFirestore
): Repository {

    override fun getAllDispositivos(result: (UiState<List<Dispositivo>>) -> Unit) {
        database.collection(FirestoreTables.DISPOSITIVO)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    result.invoke(UiState.Failure(error.localizedMessage))
                } else {
                    val dispositivos = arrayListOf<Dispositivo>()
                    for (document in querySnapshot!!) {
                        val dispositivo = document.toObject(Dispositivo::class.java)
                        dispositivos.add(dispositivo)
                    }
                    result.invoke(UiState.Success(dispositivos))
                }
            }
    }

    override fun getDispositivo(dispositivo: String, result: (UiState<Dispositivo>) -> Unit) {
        database.collection(FirestoreTables.DISPOSITIVO).document(dispositivo)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    result.invoke(UiState.Failure(error.localizedMessage))
                } else {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val disp = documentSnapshot.toObject(Dispositivo::class.java)!!
                        result.invoke(UiState.Success(disp))
                    } else {
                        result.invoke(UiState.Failure("Documento não encontrado"))
                    }
                }
            }
    }

    override fun addDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit) {
//        val document = database.collection(FirestoreTables.DISPOSITIVO).document()
//        dispositivo.id = document.id
//        document
//            .set(dispositivo)
//            .addOnSuccessListener {
//                result.invoke(
//                    UiState.Success("Dispositivo foi criado com sucesso")
//                )
//            }
//            .addOnFailureListener{
//                UiState.Failure(
//                    it.localizedMessage
//                )
//            }
    }

    override fun updateDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.DISPOSITIVO).document(dispositivo.id)
        document
            .set(dispositivo)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Dispositivo foi atualizado com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }

    }

    override fun deleteDispositivo(dispositivo: Dispositivo, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.DISPOSITIVO).document(dispositivo.id)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Dispositivo foi excluído com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }
    }

    override fun getAllAmbiente(result: (UiState<List<Ambiente>>) -> Unit) {
        database.collection(FirestoreTables.AMBIENTE)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    result.invoke(UiState.Failure(error.localizedMessage))
                } else {
                    val ambientes = arrayListOf<Ambiente>()
                    for (document in querySnapshot!!) {
                        val ambiente = document.toObject(Ambiente::class.java)
                        ambientes.add(ambiente)
                    }
                    result.invoke(UiState.Success(ambientes))
                }
            }
    }

    override fun getAmbiente(ambiente: DocumentReference, result: (UiState<Ambiente>) -> Unit){
        ambiente.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                result.invoke(UiState.Failure(error.localizedMessage))
            } else {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val amb = documentSnapshot.toObject(Ambiente::class.java)!!
                    result.invoke(UiState.Success(amb))
                } else {
                    result.invoke(UiState.Failure("Documento "+ambiente+" não encontrado"))
                }
            }
        }
    }

    override fun addAmbiente(ambiente: Ambiente, result: (UiState<String>) -> Unit): String {
        val document = database.collection(FirestoreTables.AMBIENTE).document()
        ambiente.id = document.id
        document
            .set(ambiente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Ambiente foi criado com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }
        return ambiente.id
    }

    override fun updateAmbiente(ambiente: Ambiente, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.AMBIENTE).document(ambiente.id)
        document
            .set(document)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Ambiente foi atualizado com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }
    }

    override fun deleteAmbiente() {
        TODO("Not yet implemented")
    }

}