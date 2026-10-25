package com.duoc.galpones.domain.repository

import com.duoc.galpones.domain.model.Observacion
import kotlinx.coroutines.flow.Flow

/** Contrato para leer y guardar observaciones. Lo implementa ObservacionRepositoryImpl. */
interface ObservacionRepository {
    fun observarPorGalpon(galponId: Int): Flow<List<Observacion>>
    suspend fun registrar(observacion: Observacion): Long
}
