package com.duoc.galpones.ui.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.GalponesApp
import com.duoc.galpones.domain.model.EstadoGalpon

@Composable
fun AlertasScreen(
    modifier: Modifier = Modifier,
    onAlertaClick: (Long) -> Unit = {},
    viewModel: AlertasViewModel = viewModel(
        factory = AlertasViewModel.Factory(
            (LocalContext.current.applicationContext as GalponesApp)
                .container.alertaRepository
        )
    )
) {
    val estado by viewModel.estado.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            estado.cargando -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
            estado.alertas.isEmpty() -> Text(
                text = "No hay alertas activas",
                modifier = Modifier.align(Alignment.Center)
            )
            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Alertas activas",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                items(estado.alertas, key = { it.id }) { alerta ->
                    TarjetaAlerta(
                        alerta = alerta,
                        onClick = { onAlertaClick(alerta.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaAlerta(alerta: AlertaUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(colorNivel(alerta.nivel))
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${alerta.galpon} · ${alerta.nivelTexto}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Estado: ${alerta.estadoTexto}")
                Text("Temperatura: ${alerta.temperatura} · Humedad: ${alerta.humedad}")
                Text(
                    text = "${alerta.fecha} · ${alerta.hora}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun colorNivel(nivel: EstadoGalpon): Color = when (nivel) {
    EstadoGalpon.CRITICO -> Color(0xFFD32F2F)
    EstadoGalpon.ADVERTENCIA -> Color(0xFFF9A825)
    EstadoGalpon.NORMAL -> Color(0xFF2E7D32)
}