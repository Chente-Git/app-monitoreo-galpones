package com.duoc.galpones.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Fila de la tabla mediciones. Tiene índice por galponId para filtrar el historial. */
@Entity(tableName = "mediciones", indices = [Index("galponId")])
data class MedicionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val galponId: Int,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long
)
