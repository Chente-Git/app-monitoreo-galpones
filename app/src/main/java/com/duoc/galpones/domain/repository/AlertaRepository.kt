package com.duoc.galpones.domain.repository

import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import kotlinx.coroutines.flow.Flow

/** Contrato para leer y actualizar alertas. Lo implementa AlertaRepositoryImpl con Room. */
interface AlertaRepository {
    fun observarTodas(): Flow<List<Alerta>>
    /** Alertas que no están resueltas. */
    fun observarActivas(): Flow<List<Alerta>>
    /** Cantidad de alertas en un estado. La usa el resumen para jefatura. */
    fun contarPorEstado(estado: EstadoAlerta): Flow<Int>
    suspend fun obtenerPorId(id: Long): Alerta?
    suspend fun actualizarEstado(id: Long, estado: EstadoAlerta)

    /** Carga las alertas de la FakeApi a Room solo si la tabla está vacía. */
    suspend fun cargarInicialesSiVacio()
}