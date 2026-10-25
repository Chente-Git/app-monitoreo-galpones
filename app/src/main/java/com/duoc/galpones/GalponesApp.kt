package com.duoc.galpones

import android.app.Application
import com.duoc.galpones.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Clase de la aplicación: crea el contenedor y carga los datos iniciales al abrir la app. */
class GalponesApp : Application() {

    lateinit var container: AppContainer
        private set

    /** Corrutinas de la carga inicial: van en segundo plano y si una falla no cancela la otra. */
    private val scopeApp = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        scopeApp.launch { container.alertaRepository.cargarInicialesSiVacio() }
        scopeApp.launch { container.galponRepository.cargarInicialesSiVacio() }
    }
}
