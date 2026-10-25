package com.duoc.galpones.data.repository

import com.duoc.galpones.data.local.dao.ObservacionDao
import com.duoc.galpones.data.mapper.toDomain
import com.duoc.galpones.data.mapper.toEntity
import com.duoc.galpones.domain.model.Observacion
import com.duoc.galpones.domain.repository.ObservacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Guarda y lee observaciones en Room, convirtiendo entre entidad y modelo de dominio. */
class ObservacionRepositoryImpl(
    private val dao: ObservacionDao
) : ObservacionRepository {

    override fun observarPorGalpon(galponId: Int): Flow<List<Observacion>> =
        dao.observarPorGalpon(galponId).map { lista -> lista.map { it.toDomain() } }

    override suspend fun registrar(observacion: Observacion): Long =
        dao.insertar(observacion.toEntity())
}
