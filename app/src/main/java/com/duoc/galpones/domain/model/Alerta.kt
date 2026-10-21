package com.duoc.galpones.domain.model

data class Alerta(
    val id: Long = 0,
    val galponId: Int,
    val nivel: EstadoGalpon,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long,
    val estado: EstadoAlerta = EstadoAlerta.PENDIENTE
)
