package com.duoc.galpones.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Fila de la tabla observaciones. Tiene índice por galponId para listarlas por galpón. */
@Entity(tableName = "observaciones", indices = [Index("galponId")])
data class ObservacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val galponId: Int,
    val descripcion: String,
    val fechaHora: Long
)
