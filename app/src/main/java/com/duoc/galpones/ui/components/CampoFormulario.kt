package com.duoc.galpones.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    lineasMinimas: Int = 1,
    ocultarTexto: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        leadingIcon = {
            Icon(imageVector = icono, contentDescription = null)
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = error != null,
                enter = scaleIn(tween(200)) + fadeIn(tween(200)),
                exit = scaleOut(tween(150)) + fadeOut(tween(150))
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingText = {
            AnimatedContent(
                targetState = error ?: ayuda,
                transitionSpec = {
                    (fadeIn(tween(200)) + slideInVertically(tween(200)) { -it / 2 }) togetherWith
                            fadeOut(tween(150))
                },
                label = "textoApoyo"
            ) { texto ->
                if (texto != null) Text(texto)
            }
        },
        isError = error != null,
        singleLine = unaLinea,
        minLines = lineasMinimas,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        visualTransformation = if (ocultarTexto) PasswordVisualTransformation() else VisualTransformation.None,
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