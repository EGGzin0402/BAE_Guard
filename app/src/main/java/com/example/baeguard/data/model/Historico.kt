package com.example.baeguard.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Historico(
    var CO2: Boolean = false,
    var GLP: Boolean = false,
    var temperatura: Int = 0,
    var umidade: Int = 0,
    var ambiente: String = "",
    var dispositivo: String = "",
    @ServerTimestamp
    var hora: Date = Date()
)
