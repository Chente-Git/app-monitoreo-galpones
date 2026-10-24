package com.duoc.galpones.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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

/**
 * Semáforo de estado. El color cambia con una transición cuando el estado cambia
 * (por ejemplo, al editar los umbrales) y el punto pulsa mientras el estado es crítico,
 * para llamar la atención sin depender solo del color.
 */
@Composable
fun SemaforoEstado(
    estado: EstadoGalpon,
    modifier: Modifier = Modifier,
    mostrarTexto: Boolean = true
) {
    val color by animateColorAsState(
        targetValue = colorSemaforo(estado),
        animationSpec = tween(durationMillis = 600),
        label = "colorSemaforo"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PuntoSemaforo(color = color, pulsar = estado == EstadoGalpon.CRITICO)
        if (mostrarTexto) {
            Text(
                text = textoEstado(estado),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun PuntoSemaforo(color: Color, pulsar: Boolean) {
    // La animación infinita solo existe mientras el estado es crítico.
    val escala = if (pulsar) {
        val transicion = rememberInfiniteTransition(label = "pulsoCritico")
        val valor by transicion.animateFloat(
            initialValue = 1f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 700),
                repeatMode = RepeatMode.Reverse
            ),
            label = "escalaPulso"
        )
        valor
    } else {
        1f
    }

    Box(
        modifier = Modifier
            .size(14.dp)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
            .clip(CircleShape)
            .background(color)
    )
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
