package com.duoc.galpones.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duoc.galpones.data.local.dao.AccionDao
import com.duoc.galpones.data.local.dao.AlertaDao
import com.duoc.galpones.data.local.dao.GalponDao
import com.duoc.galpones.data.local.dao.MedicionDao
import com.duoc.galpones.data.local.dao.ObservacionDao
import com.duoc.galpones.data.local.entity.AccionEntity
import com.duoc.galpones.data.local.entity.AlertaEntity
import com.duoc.galpones.data.local.entity.GalponEntity
import com.duoc.galpones.data.local.entity.MedicionEntity
import com.duoc.galpones.data.local.entity.ObservacionEntity

/** Base de datos Room. Si sube la versión se borra y se recarga desde la FakeApi. */
@Database(
    entities = [
        AlertaEntity::class,
        AccionEntity::class,
        GalponEntity::class,
        MedicionEntity::class,
        ObservacionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alertaDao(): AlertaDao
    abstract fun accionDao(): AccionDao
    abstract fun galponDao(): GalponDao
    abstract fun medicionDao(): MedicionDao
    abstract fun observacionDao(): ObservacionDao

    companion object {
        // Volatile y synchronized evitan crear dos bases si dos hilos la piden a la vez.
        @Volatile
        private var instancia: AppDatabase? = null

        /** Entrega siempre la misma base de datos: hay una sola instancia para toda la app. */
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
