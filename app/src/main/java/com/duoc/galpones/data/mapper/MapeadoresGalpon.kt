package com.duoc.galpones.data.mapper

import com.duoc.galpones.data.local.entity.GalponEntity
import com.duoc.galpones.data.local.entity.MedicionEntity
import com.duoc.galpones.data.local.entity.ObservacionEntity
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion
import com.duoc.galpones.domain.model.Observacion

fun GalponEntity.toDomain() = Galpon(
    id = id, granja = granja, nombre = nombre,
    tempMin = tempMin, tempMax = tempMax, humMin = humMin, humMax = humMax
)

fun Galpon.toEntity() = GalponEntity(
    id = id, granja = granja, nombre = nombre,
    tempMin = tempMin, tempMax = tempMax, humMin = humMin, humMax = humMax
)

fun MedicionEntity.toDomain() = Medicion(
    id = id, galponId = galponId, temperatura = temperatura,
    humedad = humedad, fechaHora = fechaHora
)

fun Medicion.toEntity() = MedicionEntity(
    id = id, galponId = galponId, temperatura = temperatura,
    humedad = humedad, fechaHora = fechaHora
)

fun ObservacionEntity.toDomain() = Observacion(
    id = id, galponId = galponId, descripcion = descripcion, fechaHora = fechaHora
)

fun Observacion.toEntity() = ObservacionEntity(
    id = id, galponId = galponId, descripcion = descripcion, fechaHora = fechaHora
)
