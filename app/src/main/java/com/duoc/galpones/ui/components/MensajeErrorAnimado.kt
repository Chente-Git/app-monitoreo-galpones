package com.duoc.galpones.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Mensaje de error con ícono que aparece y desaparece con animación. */
@Composable
fun MensajeErrorAnimado(mensaje: String?, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = mensaje,
        modifier = modifier,
        transitionSpec = {
            (fadeIn(tween(200)) + expandVertically(tween(200))) togetherWith
                    (fadeOut(tween(150)) + shrinkVertically(tween(150)))
        },
        label = "mensajeError"
    ) { texto ->
        if (texto != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = texto,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}