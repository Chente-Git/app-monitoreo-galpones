package com.duoc.galpones

import android.app.Application
import com.duoc.galpones.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GalponesApp : Application() {

    lateinit var container: AppContainer
        private set

    private val scopeApp = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        scopeApp.launch { container.alertaRepository.cargarInicialesSiVacio() }
    }
}