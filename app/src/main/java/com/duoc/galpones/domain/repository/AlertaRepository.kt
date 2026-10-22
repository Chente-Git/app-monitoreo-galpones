package com.duoc.galpones.domain.repository

import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import kotlinx.coroutines.flow.Flow

interface AlertaRepository {
    fun observarTodas(): Flow<List<Alerta>>
    fun observarActivas(): Flow<List<Alerta>>
    fun contarPorEstado(estado: EstadoAlerta): Flow<Int>
    suspend fun obtenerPorId(id: Long): Alerta?
    suspend fun actualizarEstado(id: Long, estado: EstadoAlerta)

    /** Carga las alertas de la FakeApi a Room solo si la tabla está vacía. */
    suspend fun cargarInicialesSiVacio()
}