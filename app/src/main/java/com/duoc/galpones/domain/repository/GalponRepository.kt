package com.duoc.galpones.domain.repository

import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion
import kotlinx.coroutines.flow.Flow

interface GalponRepository {
    fun observarGalpones(): Flow<List<Galpon>>
    fun observarGalpon(id: Int): Flow<Galpon?>

    /** Última medición de cada galpón. */
    fun observarUltimasMediciones(): Flow<List<Medicion>>
    fun observarUltimaMedicion(galponId: Int): Flow<Medicion?>

    /** Historial de mediciones, de la más reciente a la más antigua. Con galponId nulo trae todos. */
    fun observarMediciones(galponId: Int?): Flow<List<Medicion>>

    suspend fun actualizarUmbrales(
        id: Int,
        tempMin: Double,
        tempMax: Double,
        humMin: Double,
        humMax: Double
    )

    /** Carga galpones y mediciones de la FakeApi a Room solo si las tablas están vacías. */
    suspend fun cargarInicialesSiVacio()
}
