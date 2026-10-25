package com.duoc.galpones.domain.model

/** Nota escrita por una persona sobre lo que vio en un galpón. */
data class Observacion(
    val id: Long = 0,
    val galponId: Int,
    val descripcion: String,
    val fechaHora: Long
)