package com.duoc.galpones.ui.galpones.inicio

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.ui.components.EstadoVacio
import com.duoc.galpones.ui.components.TarjetaGalpon
import com.duoc.galpones.ui.components.contenedorApp
import com.duoc.galpones.ui.permisos.DialogoPermisoRechazado
import com.duoc.galpones.ui.permisos.PermisosSesion
import com.duoc.galpones.ui.permisos.ResultadoPermiso
import com.duoc.galpones.ui.permisos.rememberManejadorPermiso

/** Lista de galpones con su semáforo. También pide el permiso de notificaciones y ofrece el menú. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onGalponClick: (Int) -> Unit,
    onAlertas: () -> Unit,
    onHistorial: () -> Unit,
    onUmbrales: () -> Unit,
    onObservacion: () -> Unit,
    onResumen: (() -> Unit)?,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = viewModel(
        factory = InicioViewModel.Factory(
            contenedorApp().galponRepository,
            contenedorApp().notificadorCritico,
            contenedorApp().vibrador
        )
    )
) {
    val estado by viewModel.estado.collectAsState()
    var menuAbierto by remember { mutableStateOf(false) }

    // El permiso de notificaciones se pide aquí, una vez por sesión, porque es la pantalla
    // donde se detectan los estados críticos.
    var mostrarDialogo by remember { mutableStateOf(false) }
    var rechazoPermanente by remember { mutableStateOf(false) }

    val manejadorPermiso = rememberManejadorPermiso(
        Manifest.permission.POST_NOTIFICATIONS
    ) { resultado ->
        when (resultado) {
            // Al conceder, avisa de inmediato los galpones que ya estaban críticos.
            ResultadoPermiso.CONCEDIDO -> {
                mostrarDialogo = false
                viewModel.reintentarAvisos()
            }
            ResultadoPermiso.RECHAZADO -> {
                rechazoPermanente = false
                mostrarDialogo = true
            }
            // Rechazo permanente: el diálogo ofrece abrir los ajustes en vez de reintentar.
            ResultadoPermiso.RECHAZADO_PERMANENTE -> {
                rechazoPermanente = true
                mostrarDialogo = true
            }
        }
    }

    // Pide el permiso una sola vez por sesión, aunque se vuelva a esta pantalla.
    LaunchedEffect(Unit) {
        if (!PermisosSesion.notificacionesSolicitadas) {
            PermisosSesion.notificacionesSolicitadas = true
            manejadorPermiso.solicitar()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Galpones") },
                actions = {
                    IconButton(onClick = onAlertas) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Ver alertas"
                        )
                    }
                    Box {
                        IconButton(onClick = { menuAbierto = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Más opciones"
                            )
                        }
                        DropdownMenu(
                            expanded = menuAbierto,
                            onDismissRequest = { menuAbierto = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Historial") },
                                onClick = { menuAbierto = false; onHistorial() }
                            )
                            DropdownMenuItem(
                                text = { Text("Umbrales") },
                                onClick = { menuAbierto = false; onUmbrales() }
                            )
                            DropdownMenuItem(
                                text = { Text("Registrar observación") },
                                onClick = { menuAbierto = false; onObservacion() }
                            )
                            if (onResumen != null) {
                                DropdownMenuItem(
                                    text = { Text("Resumen para jefatura") },
                                    onClick = { menuAbierto = false; onResumen() }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = { menuAbierto = false; onCerrarSesion() }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                estado.cargando -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                estado.galpones.isEmpty() -> EstadoVacio(
                    titulo = "No hay galpones registrados",
                    mensaje = "Cuando se carguen los datos, aparecerán aquí."
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(estado.galpones, key = { it.id }) { galpon ->
                        TarjetaGalpon(
                            nombre = galpon.nombre,
                            granja = galpon.granja,
                            temperatura = galpon.temperatura,
                            humedad = galpon.humedad,
                            actualizacion = galpon.actualizacion,
                            estado = galpon.estado,
                            onClick = { onGalponClick(galpon.id) }
                        )
                    }
                }
            }

            if (mostrarDialogo) {
                DialogoPermisoRechazado(
                    mensaje = "Las notificaciones te avisan cuando un galpón entra en estado crítico. " +
                            "Sin este permiso no recibirás esos avisos.",
                    permanente = rechazoPermanente,
                    onReintentar = {
                        mostrarDialogo = false
                        manejadorPermiso.solicitar()
                    },
                    onAbrirAjustes = {
                        mostrarDialogo = false
                        manejadorPermiso.abrirAjustes()
                    },
                    onCerrar = { mostrarDialogo = false }
                )
            }
        }
    }
}
