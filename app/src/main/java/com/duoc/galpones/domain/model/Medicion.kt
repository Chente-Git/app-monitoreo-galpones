package com.duoc.galpones.domain.model

data class Medicion(
    val id: Long = 0,
    val galponId: Int,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long
)