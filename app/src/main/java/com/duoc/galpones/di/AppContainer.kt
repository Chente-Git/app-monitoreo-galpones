package com.duoc.galpones.di

import android.content.Context
import com.duoc.galpones.data.fakeapi.FakeAlertasApi
import com.duoc.galpones.data.local.AppDatabase
import com.duoc.galpones.data.repository.AccionRepositoryImpl
import com.duoc.galpones.data.repository.AlertaRepositoryImpl
import com.duoc.galpones.domain.repository.AccionRepository
import com.duoc.galpones.domain.repository.AlertaRepository

/**
 * Contenedor de dependencias manual. Los ViewModels obtienen de aquí
 * la base de datos y los repositorios.
 */
class AppContainer(context: Context) {

    val database: AppDatabase by lazy { AppDatabase.obtener(context) }

    val alertaRepository: AlertaRepository by lazy {
        AlertaRepositoryImpl(database.alertaDao(), FakeAlertasApi)
    }

    val accionRepository: AccionRepository by lazy {
        AccionRepositoryImpl(database.accionDao())
    }
}