package com.example.baeguard.data.model

import com.google.firebase.firestore.DocumentReference

data class Dispositivo(
    var id: String = "",
    var nome: String = "",
    var CO2: Boolean = false,
    var GLP: Boolean = false,
    var chama: Boolean = false,
    var URLcam: String = "",
    var temperatura: Int = 0,
    var umidade: Int = 0,
    var ambiente: DocumentReference? = null
)