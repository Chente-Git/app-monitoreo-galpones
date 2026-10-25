package com.duoc.galpones.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.galpones.data.local.entity.GalponEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GalponDao {

    @Query("SELECT * FROM galpones ORDER BY id")
    fun observarTodos(): Flow<List<GalponEntity>>

    @Query("SELECT * FROM galpones WHERE id = :id")
    fun observarPorId(id: Int): Flow<GalponEntity?>

    @Query("SELECT COUNT(*) FROM galpones")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(galpones: List<GalponEntity>)

    @Query(
        "UPDATE galpones SET tempMin = :tempMin, tempMax = :tempMax, " +
                "humMin = :humMin, humMax = :humMax WHERE id = :id"
    )
    suspend fun actualizarUmbrales(
        id: Int,
        tempMin: Double,
        tempMax: Double,
        humMin: Double,
        humMax: Double
    )
}
