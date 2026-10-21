package com.duoc.galpones.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.duoc.galpones.data.local.entity.AccionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccionDao {

    @Query("SELECT * FROM acciones ORDER BY fechaHora DESC")
    fun observarTodas(): Flow<List<AccionEntity>>

    @Query("SELECT * FROM acciones WHERE alertaId = :alertaId ORDER BY fechaHora DESC")
    fun observarPorAlerta(alertaId: Long): Flow<List<AccionEntity>>

    @Insert
    suspend fun insertar(accion: AccionEntity): Long
}
