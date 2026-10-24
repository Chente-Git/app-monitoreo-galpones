package com.duoc.galpones.ui.galpones.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.ui.components.EstadoVacio
import com.duoc.galpones.ui.components.SemaforoEstado
import com.duoc.galpones.ui.components.contenedorApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    galponId: Int,
    onVolver: () -> Unit,
    onHistorial: () -> Unit,
    onUmbrales: () -> Unit,
    onObservacion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalleViewModel = viewModel(
        factory = DetalleViewModel.Factory(
            galponId,
            contenedorApp().galponRepository,
            contenedorApp().observacionRepository
        )
    )
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(estado.nombre.ifEmpty { "Detalle del galpón" }) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
                !estado.encontrado -> EstadoVacio(
                    titulo = "No se encontró el galpón",
                    mensaje = "Vuelve a la lista e intenta de nuevo."
                )
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = estado.granja,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                SemaforoEstado(estado = estado.estado)
                            }
                            Text(
                                text = "Temperatura: ${estado.temperatura}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Humedad: ${estado.humedad}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "Actualizado: ${estado.actualizacion}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Umbrales configurados",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Temperatura: ${estado.umbralTemperatura}")
                            Text("Humedad: ${estado.umbralHumedad}")
                        }
                    }

                    Button(onClick = onHistorial, modifier = Modifier.fillMaxWidth()) {
                        Text("Ver historial")
                    }
                    OutlinedButton(onClick = onUmbrales, modifier = Modifier.fillMaxWidth()) {
                        Text("Editar umbrales")
                    }
                    OutlinedButton(onClick = onObservacion, modifier = Modifier.fillMaxWidth()) {
                        Text("Registrar observación")
                    }

                    Text(
                        text = "Observaciones recientes",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (estado.observaciones.isEmpty()) {
                        Text(
                            text = "Aún no hay observaciones para este galpón.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        estado.observaciones.take(5).forEach { observacion ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(observacion.texto)
                                    Text(
                                        text = observacion.fecha,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
