package com.duoc.galpones.domain.model

/** Lo que alguien hizo para atender una alerta, por ejemplo revisar la ventilación. */
data class Accion(
    val id: Long = 0,
    val alertaId: Long,
    val usuarioId: Int,
    val tipo: TipoAccion,
    val descripcion: String,
    val fechaHora: Long
)
