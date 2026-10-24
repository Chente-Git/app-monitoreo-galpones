package com.duoc.galpones.domain

import com.duoc.galpones.domain.model.Usuario

/** Guarda en memoria al usuario que inició sesión. */
object SesionActual {

    var usuario: Usuario? = null
        private set

    fun iniciar(usuario: Usuario) {
        this.usuario = usuario
    }

    fun cerrar() {
        usuario = null
    }
}