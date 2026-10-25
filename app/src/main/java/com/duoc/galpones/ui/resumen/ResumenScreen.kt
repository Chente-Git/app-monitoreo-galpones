package com.duoc.galpones.ui.resumen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.GalponesApp
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.ui.components.BarraSuperiorVolver
import com.duoc.galpones.ui.components.colorSemaforo

@Composable
fun ResumenScreen(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResumenViewModel = viewModel(
        factory = ResumenViewModel.Factory(
            (LocalContext.current.applicationContext as GalponesApp)
                .container.alertaRepository
        )
    )
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { BarraSuperiorVolver(titulo = "Resumen para jefatura", onVolver = onVolver) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (estado.cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TarjetaConteo("Pendientes", estado.pendientes, Modifier.weight(1f))
                            TarjetaConteo("En atención", estado.enAtencion, Modifier.weight(1f))
                            TarjetaConteo("Resueltas", estado.resueltas, Modifier.weight(1f))
                        }
                    }
                    item {
                        Text(
                            text = "Alertas críticas activas",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    if (estado.criticas.isEmpty()) {
                        item { Text("No hay alertas críticas activas") }
                    } else {
                        items(estado.criticas, key = { it.id }) { alerta ->
                            TarjetaCritica(alerta)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaConteo(titulo: String, cantidad: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TarjetaCritica(alerta: AlertaCriticaUi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(colorSemaforo(EstadoGalpon.CRITICO))
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = alerta.titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Estado: ${alerta.estado}")
                Text(alerta.detalle)
                Text(
                    text = alerta.fecha,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}