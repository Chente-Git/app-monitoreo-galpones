package com.duoc.galpones.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "acciones", indices = [Index("alertaId")])
data class AccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alertaId: Long,
    val usuarioId: Int,
    val tipo: String,
    val descripcion: String,
    val fechaHora: Long
)
