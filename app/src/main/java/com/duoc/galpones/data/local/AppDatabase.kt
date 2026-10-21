package com.duoc.galpones.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duoc.galpones.data.local.dao.AccionDao
import com.duoc.galpones.data.local.dao.AlertaDao
import com.duoc.galpones.data.local.entity.AccionEntity
import com.duoc.galpones.data.local.entity.AlertaEntity

// Integrante A: agrega aquí sus entidades (Galpon, Medicion, Observacion) y sus DAO,
// y sube la versión en 1.
@Database(
    entities = [AlertaEntity::class, AccionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alertaDao(): AlertaDao
    abstract fun accionDao(): AccionDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "galpones.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { instancia = it }
            }
    }
}
