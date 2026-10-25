package com.duoc.galpones.data.mapper

import com.duoc.galpones.data.local.entity.AccionEntity
import com.duoc.galpones.data.local.entity.AlertaEntity
import com.duoc.galpones.domain.model.Accion
import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.TipoAccion

/** Convierten entre entidades de Room y modelos de dominio, para que domain no dependa de Room. */
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
