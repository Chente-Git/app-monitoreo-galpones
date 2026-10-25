package com.duoc.galpones.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Opción de la lista de galpones: el id se guarda y el texto se muestra. */
data class OpcionGalpon(val id: Int, val texto: String)

/** Lista de galpones para elegir uno, con mensaje de error animado. */
@Composable
fun SelectorGalpon(
    opciones: List<OpcionGalpon>,
    seleccionado: Int?,
    onSeleccionar: (Int) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "Galpón", style = MaterialTheme.typography.titleMedium)
        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeleccionar(opcion.id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = seleccionado == opcion.id,
                    onClick = null
                )
                Text(
                    text = opcion.texto,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        MensajeErrorAnimado(error)
    }
}
