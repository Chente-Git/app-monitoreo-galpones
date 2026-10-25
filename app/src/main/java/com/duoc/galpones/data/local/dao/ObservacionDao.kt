package com.duoc.galpones.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duoc.galpones.data.local.entity.ObservacionEntity
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla observaciones. Las usa ObservacionRepositoryImpl. */
@Dao
interface ObservacionDao {

    @Query("SELECT * FROM observaciones WHERE galponId = :galponId ORDER BY fechaHora DESC")
    fun observarPorGalpon(galponId: Int): Flow<List<ObservacionEntity>>

    @Insert
    suspend fun insertar(observacion: ObservacionEntity): Long
}
