package com.duoc.galpones.ui.galpones.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

/** Mediciones de todos los galpones o de uno solo, filtradas con chips. Cada una lleva su semáforo. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistorialViewModel = viewModel(
        factory = HistorialViewModel.Factory(contenedorApp().galponRepository)
    )
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = estado.seleccionado == null,
                        onClick = { viewModel.seleccionar(null) },
                        label = { Text("Todos") }
                    )
                }
                items(estado.filtros, key = { it.id }) { filtro ->
                    FilterChip(
                        selected = estado.seleccionado == filtro.id,
                        onClick = { viewModel.seleccionar(filtro.id) },
                        label = { Text(filtro.nombre) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    estado.cargando -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                    estado.mediciones.isEmpty() -> EstadoVacio(
                        titulo = "Sin mediciones",
                        mensaje = "No hay mediciones registradas para este filtro."
                    )
                    else -> LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(estado.mediciones, key = { it.id }) { medicion ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = medicion.galpon,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("Temp: ${medicion.temperatura} · Humedad: ${medicion.humedad}")
                                        Text(
                                            text = medicion.fechaHora,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    SemaforoEstado(estado = medicion.estado)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
