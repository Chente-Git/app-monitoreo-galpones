package com.duoc.galpones.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.galpones.data.local.entity.MedicionEntity
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla mediciones. Las usa GalponRepositoryImpl. */
@Dao
interface MedicionDao {

    /** Última medición de cada galpón: une cada galpón con su fecha más reciente. */
    @Query(
        "SELECT m.* FROM mediciones m " +
                "INNER JOIN (SELECT galponId, MAX(fechaHora) AS ultima FROM mediciones GROUP BY galponId) u " +
                "ON m.galponId = u.galponId AND m.fechaHora = u.ultima"
    )
    fun observarUltimas(): Flow<List<MedicionEntity>>

    @Query("SELECT * FROM mediciones WHERE galponId = :galponId ORDER BY fechaHora DESC LIMIT 1")
    fun observarUltima(galponId: Int): Flow<MedicionEntity?>

    @Query("SELECT * FROM mediciones ORDER BY fechaHora DESC")
    fun observarTodas(): Flow<List<MedicionEntity>>

    @Query("SELECT * FROM mediciones WHERE galponId = :galponId ORDER BY fechaHora DESC")
    fun observarPorGalpon(galponId: Int): Flow<List<MedicionEntity>>

    /** Sirve para saber si la tabla está vacía y cargar los datos iniciales. */
    @Query("SELECT COUNT(*) FROM mediciones")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(mediciones: List<MedicionEntity>)
}
