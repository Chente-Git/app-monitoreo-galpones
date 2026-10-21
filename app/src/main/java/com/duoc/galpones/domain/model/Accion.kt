package com.duoc.galpones.domain.model

data class Accion(
    val id: Long = 0,
    val alertaId: Long,
    val usuarioId: Int,
    val tipo: TipoAccion,
    val descripcion: String,
    val fechaHora: Long
)
