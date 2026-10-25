package com.duoc.galpones.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.galpones.data.local.entity.AlertaEntity
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla alertas. Devuelven Flow: la pantalla se actualiza sola al cambiar los datos. */
@Dao
interface AlertaDao {

    @Query("SELECT * FROM alertas ORDER BY fechaHora DESC")
    fun observarTodas(): Flow<List<AlertaEntity>>

    /** Alertas que no están resueltas. El estado "resuelta" llega como parámetro desde el repositorio. */
    @Query("SELECT * FROM alertas WHERE estado != :estadoResuelta ORDER BY fechaHora DESC")
    fun observarActivas(estadoResuelta: String): Flow<List<AlertaEntity>>

    @Query("SELECT * FROM alertas WHERE id = :id")
    suspend fun obtenerPorId(id: Long): AlertaEntity?

    @Query("SELECT COUNT(*) FROM alertas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    /** Sirve para saber si la tabla está vacía y cargar los datos iniciales. */
    @Query("SELECT COUNT(*) FROM alertas")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(alerta: AlertaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(alertas: List<AlertaEntity>)

    @Query("UPDATE alertas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Long, estado: String)
}
