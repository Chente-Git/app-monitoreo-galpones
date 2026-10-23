package com.duoc.galpones.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.ui.theme.MonitoreoGalponesTheme
import com.duoc.galpones.ui.theme.SemaforoAdvertencia
import com.duoc.galpones.ui.theme.SemaforoCritico
import com.duoc.galpones.ui.theme.SemaforoNormal

fun colorSemaforo(estado: EstadoGalpon): Color = when (estado) {
    EstadoGalpon.NORMAL -> SemaforoNormal
    EstadoGalpon.ADVERTENCIA -> SemaforoAdvertencia
    EstadoGalpon.CRITICO -> SemaforoCritico
}

fun textoEstado(estado: EstadoGalpon): String = when (estado) {
    EstadoGalpon.NORMAL -> "Normal"
    EstadoGalpon.ADVERTENCIA -> "Advertencia"
    EstadoGalpon.CRITICO -> "Crítico"
}


@Composable
fun SemaforoEstado(
    estado: EstadoGalpon,
    modifier: Modifier = Modifier,
    mostrarTexto: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(colorSemaforo(estado))
        )
        if (mostrarTexto) {
            Text(
                text = textoEstado(estado),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SemaforoEstadoPreview() {
    MonitoreoGalponesTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SemaforoEstado(EstadoGalpon.NORMAL)
            SemaforoEstado(EstadoGalpon.ADVERTENCIA)
            SemaforoEstado(EstadoGalpon.CRITICO)
        }
    }
}