package com.duoc.galpones.ui.accion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.GalponesApp
import com.duoc.galpones.domain.model.TipoAccion
import com.duoc.galpones.ui.components.CampoFormulario

@Composable
fun AccionScreen(
    alertaId: Long,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccionViewModel = viewModel(
        factory = AccionViewModel.Factory(
            alertaId,
            (LocalContext.current.applicationContext as GalponesApp)
                .container.alertaRepository,
            (LocalContext.current.applicationContext as GalponesApp)
                .container.accionRepository
        )
    )
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            viewModel.guardadoProcesado()
            onGuardado()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registrar acción",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Alerta",
            style = MaterialTheme.typography.titleMedium
        )
        if (estado.resumenAlerta.isNotEmpty()) {
            Text(text = estado.resumenAlerta)
        }
        MensajeError(estado.errorAlerta)

        Text(
            text = "Tipo de acción",
            style = MaterialTheme.typography.titleMedium
        )
        TipoAccion.values().forEach { tipo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onTipoChange(tipo) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = estado.tipo == tipo,
                    onClick = null
                )
                Text(
                    text = tipo.etiqueta(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        MensajeError(estado.errorTipo)

        CampoFormulario(
            valor = estado.descripcion,
            onValorChange = viewModel::onDescripcionChange,
            etiqueta = "Descripción",
            icono = Icons.Filled.Edit,
            error = estado.errorDescripcion,
            ayuda = "Mínimo 10 caracteres",
            unaLinea = false,
            lineasMinimas = 3
        )

        MensajeError(estado.errorGeneral)

        Button(
            onClick = viewModel::onGuardar,
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado.guardando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Guardar acción")
            }
        }

        OutlinedButton(
            onClick = onCancelar,
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}

@Composable
private fun MensajeError(mensaje: String?) {
    if (mensaje != null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

private fun TipoAccion.etiqueta(): String = when (this) {
    TipoAccion.REVISION_VENTILACION -> "Revisión de ventilación"
    TipoAccion.AJUSTE_DE_EQUIPOS -> "Ajuste de equipos"
    TipoAccion.AVISO_A_SUPERVISOR -> "Aviso a supervisor"
    TipoAccion.OTRA -> "Otra"
}