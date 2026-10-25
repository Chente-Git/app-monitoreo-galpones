package com.duoc.galpones.di

import android.content.Context
import com.duoc.galpones.data.fakeapi.FakeAlertasApi
import com.duoc.galpones.data.fakeapi.FakeGalponesApi
import com.duoc.galpones.data.local.AppDatabase
import com.duoc.galpones.data.red.MonitorRed
import com.duoc.galpones.data.repository.AccionRepositoryImpl
import com.duoc.galpones.data.repository.AlertaRepositoryImpl
import com.duoc.galpones.data.repository.GalponRepositoryImpl
import com.duoc.galpones.data.repository.ObservacionRepositoryImpl
import com.duoc.galpones.domain.repository.AccionRepository
import com.duoc.galpones.domain.repository.AlertaRepository
import com.duoc.galpones.domain.repository.GalponRepository
import com.duoc.galpones.domain.repository.ObservacionRepository
import com.duoc.galpones.platform.notificaciones.NotificadorCritico
import com.duoc.galpones.platform.vibracion.Vibrador

/**
 * Contenedor de dependencias manual. Los ViewModels obtienen de aquí
 * la base de datos y los repositorios.
 */
class AppContainer(context: Context) {

    /** Todo se crea con lazy: cada objeto nace la primera vez que alguien lo usa. */
    val database: AppDatabase by lazy { AppDatabase.obtener(context) }

    val alertaRepository: AlertaRepository by lazy {
        AlertaRepositoryImpl(database.alertaDao(), FakeAlertasApi)
    }

    val accionRepository: AccionRepository by lazy {
        AccionRepositoryImpl(database.accionDao())
    }

    val galponRepository: GalponRepository by lazy {
        GalponRepositoryImpl(database.galponDao(), database.medicionDao(), FakeGalponesApi)
    }

    val observacionRepository: ObservacionRepository by lazy {
        ObservacionRepositoryImpl(database.observacionDao())
    }

    val monitorRed: MonitorRed by lazy { MonitorRed(context.applicationContext) }

    val notificadorCritico: NotificadorCritico by lazy {
        NotificadorCritico(context.applicationContext)
    }

    val vibrador: Vibrador by lazy { Vibrador(context.applicationContext) }
}
