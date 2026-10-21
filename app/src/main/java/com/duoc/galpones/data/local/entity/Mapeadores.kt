package com.duoc.galpones.data.local.entity

import com.duoc.galpones.domain.model.Accion
import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.TipoAccion

fun AlertaEntity.toDomain() = Alerta(
    id = id,
    galponId = galponId,
    nivel = EstadoGalpon.valueOf(nivel),
    temperatura = temperatura,
    humedad = humedad,
    fechaHora = fechaHora,
    estado = EstadoAlerta.valueOf(estado)
)

fun Alerta.toEntity() = AlertaEntity(
    id = id,
    galponId = galponId,
    nivel = nivel.name,
    temperatura = temperatura,
    humedad = humedad,
    fechaHora = fechaHora,
    estado = estado.name
)

fun AccionEntity.toDomain() = Accion(
    id = id,
    alertaId = alertaId,
    usuarioId = usuarioId,
    tipo = TipoAccion.valueOf(tipo),
    descripcion = descripcion,
    fechaHora = fechaHora
)

fun Accion.toEntity() = AccionEntity(
    id = id,
    alertaId = alertaId,
    usuarioId = usuarioId,
    tipo = tipo.name,
    descripcion = descripcion,
    fechaHora = fechaHora
)
