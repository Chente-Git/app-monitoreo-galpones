package com.duoc.galpones.ui.permisos

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

enum class ResultadoPermiso { CONCEDIDO, RECHAZADO, RECHAZADO_PERMANENTE }

/** Pide un permiso y avisa el resultado. Sirve para cualquier permiso de ejecución. */
class ManejadorPermiso internal constructor(
    private val contexto: Context,
    val permiso: String,
    private val lanzar: () -> Unit,
    private val notificar: (ResultadoPermiso) -> Unit
) {
    fun estaConcedido(): Boolean {
        val noExigeEnEstaVersion = permiso == Manifest.permission.POST_NOTIFICATIONS &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        if (noExigeEnEstaVersion) return true
        return ContextCompat.checkSelfPermission(contexto, permiso) ==
                PackageManager.PERMISSION_GRANTED
    }

    /** Si ya está concedido avisa de inmediato; si no, muestra el diálogo del sistema. */
    fun solicitar() {
        if (estaConcedido()) notificar(ResultadoPermiso.CONCEDIDO) else lanzar()
    }

    /** Lleva a la pantalla de ajustes de la app, para cuando el rechazo es permanente. */
    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", contexto.packageName, null)
        )
        contexto.startActivity(intent)
    }
}

@Composable
fun rememberManejadorPermiso(
    permiso: String,
    onResultado: (ResultadoPermiso) -> Unit
): ManejadorPermiso {
    val contexto = LocalContext.current
    val resultadoActual by rememberUpdatedState(onResultado)

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        val actividad = contexto.buscarActividad()
        val puedeExplicarse = actividad != null &&
                ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)
        val resultado = when {
            concedido -> ResultadoPermiso.CONCEDIDO
            puedeExplicarse -> ResultadoPermiso.RECHAZADO
            else -> ResultadoPermiso.RECHAZADO_PERMANENTE
        }
        resultadoActual(resultado)
    }

    return remember(permiso) {
        ManejadorPermiso(
            contexto = contexto,
            permiso = permiso,
            lanzar = { lanzador.launch(permiso) },
            notificar = { resultadoActual(it) }
        )
    }
}

private fun Context.buscarActividad(): ComponentActivity? {
    var actual: Context = this
    while (actual is ContextWrapper) {
        if (actual is ComponentActivity) return actual
        actual = actual.baseContext
    }
    return null
}