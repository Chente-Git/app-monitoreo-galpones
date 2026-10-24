package com.duoc.galpones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.galpones.ui.theme.MonitoreoGalponesTheme


@Composable
fun CampoFormulario(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    ayuda: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    unaLinea: Boolean = true,
    lineasMinimas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        leadingIcon = {
            Icon(imageVector = icono, contentDescription = null)
        },
        trailingIcon = {
            if (error != null) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingText = {
            val texto = error ?: ayuda
            if (texto != null) Text(texto)
        },
        isError = error != null,
        singleLine = unaLinea,
        minLines = lineasMinimas,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun CampoFormularioPreview() {
    MonitoreoGalponesTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoFormulario(
                valor = "26",
                onValorChange = {},
                etiqueta = "Temperatura máxima (°C)",
                icono = Icons.Filled.Thermostat,
                ayuda = "Valor entre 0 y 50"
            )
            CampoFormulario(
                valor = "abc",
                onValorChange = {},
                etiqueta = "Temperatura mínima (°C)",
                icono = Icons.Filled.Thermostat,
                error = "Ingresa un número válido",
                tipoTeclado = KeyboardType.Decimal
            )
            CampoFormulario(
                valor = "",
                onValorChange = {},
                etiqueta = "Observación",
                icono = Icons.Filled.Edit,
                error = "Escribe al menos 10 caracteres",
                unaLinea = false,
                lineasMinimas = 3
            )
        }
    }
}