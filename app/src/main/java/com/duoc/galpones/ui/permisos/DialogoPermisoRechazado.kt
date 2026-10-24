package com.duoc.galpones.ui.permisos

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** Diálogo para cuando el usuario rechaza un permiso: reintentar o ir a ajustes. */
@Composable
fun DialogoPermisoRechazado(
    mensaje: String,
    permanente: Boolean,
    onReintentar: () -> Unit,
    onAbrirAjustes: () -> Unit,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Permiso necesario") },
        text = { Text(mensaje) },
        confirmButton = {
            if (permanente) {
                TextButton(onClick = onAbrirAjustes) { Text("Abrir ajustes") }
            } else {
                TextButton(onClick = onReintentar) { Text("Reintentar") }
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Ahora no") }
        }
    )
}