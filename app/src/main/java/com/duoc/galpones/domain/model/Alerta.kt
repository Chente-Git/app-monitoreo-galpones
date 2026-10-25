package com.duoc.galpones.domain.model

/** Aviso de que un galpón salió de su rango. Pasa a EN_ATENCION al registrar una acción. */
data class Alerta(
    val id: Long = 0,
    val galponId: Int,
    val nivel: EstadoGalpon,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long,
    val estado: EstadoAlerta = EstadoAlerta.PENDIENTE
)
