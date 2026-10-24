package com.duoc.galpones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.ui.theme.MonitoreoGalponesTheme


@Composable
fun TarjetaGalpon(
    nombre: String,
    granja: String,
    temperatura: String,
    humedad: String,
    actualizacion: String,
    estado: EstadoGalpon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = granja,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SemaforoEstado(estado = estado)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(text = "Temp: $temperatura", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Humedad: $humedad", style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                text = "Actualizado: $actualizacion",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TarjetaGalponPreview() {
    MonitoreoGalponesTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaGalpon(
                nombre = "Galpón 1", granja = "Granja Norte",
                temperatura = "26,5 °C", humedad = "62 %",
                actualizacion = "23-10-2026 · 19:40",
                estado = EstadoGalpon.NORMAL, onClick = {}
            )
            TarjetaGalpon(
                nombre = "Galpón 2", granja = "Granja Norte",
                temperatura = "38,2 °C", humedad = "85 %",
                actualizacion = "23-10-2026 · 19:38",
                estado = EstadoGalpon.CRITICO, onClick = {}
            )
        }
    }
}