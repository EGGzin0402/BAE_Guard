package com.example.baeguard.data.model

import com.google.firebase.firestore.DocumentReference

/*
Classe de modelagem Ambiente
 */
data class Ambiente(
    var id: String = "",
    var nome: String = "",
    var usuario: DocumentReference? = null,
)
