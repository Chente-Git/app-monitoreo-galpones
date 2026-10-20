package com.duoc.galpones.domain.model

data class Observacion(
    val id: Long = 0,
    val galponId: Int,
    val descripcion: String,
    val fechaHora: Long
)