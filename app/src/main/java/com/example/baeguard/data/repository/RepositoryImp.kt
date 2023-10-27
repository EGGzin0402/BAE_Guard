package com.example.baeguard.data.repository

import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.data.model.Historico
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.FirestoreTables
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "BAE REPOSITORY IMP"

/*
Classe de Implementação da interface do repositório
 */

class RepositoryImp(
    val database: FirebaseFirestore
): Repository {

    //Dispositivo

    override fun getAllDispositivos(user: UserData?, result: (UiState<List<Dispositivo>>) -> Unit) {
        database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.DISPOSITIVO)
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

    override fun getDispositivo(user: UserData?, dispositivo: String, result: (UiState<Dispositivo>) -> Unit) {
        database.collection(FirestoreTables.USUARIO).document(user!!.userId)
            .collection(FirestoreTables.DISPOSITIVO).document(dispositivo)
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

    override fun addDispositivo(user: UserData?, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.DISPOSITIVO).document()
        val id = document.id
        document
            .set(
                Dispositivo(
                    id = id
                )
            )
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Dispositivo foi criado com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }
    }

    override fun updateDispositivo(user: UserData?, dispositivo: Dispositivo, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.DISPOSITIVO).document(dispositivo.id)
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

    override fun deleteDispositivo(user: UserData?, dispositivo: Dispositivo, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.DISPOSITIVO).document(dispositivo.id)
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

    //Ambiente

    override fun getAllAmbiente(user: UserData?, result: (UiState<List<Ambiente>>) -> Unit) {
        database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.AMBIENTE)
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

    override fun getAmbiente(user: UserData?, ambiente: DocumentReference, result: (UiState<Ambiente>) -> Unit){
        ambiente.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                result.invoke(UiState.Failure(error.localizedMessage))
            } else {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val amb = documentSnapshot.toObject(Ambiente::class.java)!!
                    result.invoke(UiState.Success(amb))
                } else {
                    result.invoke(UiState.Failure("Documento $ambiente não encontrado"))
                }
            }
        }
    }

    override fun addAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit): String {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.AMBIENTE).document()
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

    override fun updateAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.AMBIENTE).document(ambiente.id)
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

    override fun deleteAmbiente(user: UserData?, ambiente: Ambiente, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.AMBIENTE).document(ambiente.id)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Ambiente foi excluído com sucesso")
                )
            }
            .addOnFailureListener{
                UiState.Failure(
                    it.localizedMessage
                )
            }
    }

    //Historico

    override fun getAllHistorico(user: UserData?, result: (UiState<List<Historico>>) -> Unit) {
        database.collection(FirestoreTables.USUARIO).document(user!!.userId).collection(FirestoreTables.HISTORICO)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    result.invoke(UiState.Failure(error.localizedMessage))
                } else {
                    val historico = arrayListOf<Historico>()
                    for (document in querySnapshot!!) {
                        val registro = document.toObject(Historico::class.java)
                        historico.add(registro)
                    }
                    result.invoke(UiState.Success(historico))
                }
            }
    }

}