package com.duoc.galpones.data.repository

import com.duoc.galpones.data.local.dao.AccionDao
import com.duoc.galpones.data.mapper.toDomain
import com.duoc.galpones.data.mapper.toEntity
import com.duoc.galpones.domain.model.Accion
import com.duoc.galpones.domain.repository.AccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccionRepositoryImpl(
    private val dao: AccionDao
) : AccionRepository {

    override fun observarPorAlerta(alertaId: Long): Flow<List<Accion>> =
        dao.observarPorAlerta(alertaId).map { lista -> lista.map { it.toDomain() } }

    override suspend fun registrar(accion: Accion): Long =
        dao.insertar(accion.toEntity())
}