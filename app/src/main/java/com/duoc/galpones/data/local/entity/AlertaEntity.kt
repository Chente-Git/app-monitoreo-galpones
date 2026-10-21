package com.duoc.galpones.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alertas")
data class AlertaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val galponId: Int,
    val nivel: String,
    val temperatura: Double,
    val humedad: Double,
    val fechaHora: Long,
    val estado: String
)
