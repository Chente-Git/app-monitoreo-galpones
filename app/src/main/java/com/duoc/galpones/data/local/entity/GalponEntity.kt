package com.duoc.galpones.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Fila de la tabla galpones, con sus umbrales editables. Su id viene de la FakeApi. */
@Entity(tableName = "galpones")
data class GalponEntity(
    @PrimaryKey val id: Int,
    val granja: String,
    val nombre: String,
    val tempMin: Double,
    val tempMax: Double,
    val humMin: Double,
    val humMax: Double
)
