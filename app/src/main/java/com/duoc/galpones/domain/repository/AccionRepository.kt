package com.duoc.galpones.domain.repository

import com.duoc.galpones.domain.model.Accion
import kotlinx.coroutines.flow.Flow

interface AccionRepository {
    fun observarPorAlerta(alertaId: Long): Flow<List<Accion>>
    suspend fun registrar(accion: Accion): Long
}