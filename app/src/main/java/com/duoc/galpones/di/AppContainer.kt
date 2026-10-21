package com.duoc.galpones.di

import android.content.Context
import com.duoc.galpones.data.local.AppDatabase

/**
 * Contenedor de dependencias manual. Los ViewModels obtienen de aquí
 * la base de datos y (más adelante) los repositorios.
 */
class AppContainer(context: Context) {

    val database: AppDatabase by lazy { AppDatabase.obtener(context) }

    // Repositorios: se agregan aquí a medida que se crean, por ejemplo:
    // val alertaRepository: AlertaRepository by lazy { AlertaRepositoryImpl(database.alertaDao()) }
}
