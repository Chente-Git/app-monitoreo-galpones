package com.duoc.galpones.ui.permisos

/** Recuerda mientras la app está abierta si ya se pidió el permiso, para no insistir. */
object PermisosSesion {
    var notificacionesSolicitadas: Boolean = false
}