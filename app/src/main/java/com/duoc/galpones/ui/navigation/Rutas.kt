package com.duoc.galpones.ui.navigation

object Rutas {
    const val ARG_GALPON_ID = "galponId"
    const val ARG_ALERTA_ID = "alertaId"

    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{$ARG_GALPON_ID}"
    const val HISTORIAL = "historial"
    const val UMBRALES = "umbrales"
    const val OBSERVACION = "observacion"
    const val ALERTAS = "alertas"
    const val ACCION = "accion/{$ARG_ALERTA_ID}"
    const val RESUMEN = "resumen"

    fun detalle(galponId: Int) = "detalle/$galponId"
    fun accion(alertaId: Long) = "accion/$alertaId"
}
