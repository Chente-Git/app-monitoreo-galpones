package com.duoc.galpones.domain.model

/** Una lectura de temperatura y humedad de un galpón en un momento dado. */
data class Medicion(
    val id: Long = 0,
    val galponId: Int,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long
)