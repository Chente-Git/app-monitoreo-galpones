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
