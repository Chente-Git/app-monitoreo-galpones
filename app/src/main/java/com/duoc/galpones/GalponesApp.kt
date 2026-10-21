package com.duoc.galpones

import android.app.Application
import com.duoc.galpones.di.AppContainer

class GalponesApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
