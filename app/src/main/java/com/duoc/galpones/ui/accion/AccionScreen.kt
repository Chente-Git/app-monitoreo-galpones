package com.duoc.galpones.ui.accion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.GalponesApp
import com.duoc.galpones.domain.model.TipoAccion
import com.duoc.galpones.ui.components.CampoFormulario
import com.duoc.galpones.ui.components.MensajeErrorAnimado
import kotlinx.coroutines.delay

/** Formulario para registrar qué se hizo ante una alerta. Al guardar confirma y vuelve atrás. */
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
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Muestra la confirmación 2,5 s y vuelve. Mientras tanto los botones quedan desactivados.
    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            mostrarConfirmacion = true
            delay(2500)
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
        MensajeErrorAnimado(estado.errorAlerta)

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
        MensajeErrorAnimado(estado.errorTipo)

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

        MensajeErrorAnimado(estado.errorGeneral)

        AnimatedVisibility(
            visible = mostrarConfirmacion,
            enter = fadeIn(tween(250)) + scaleIn(tween(250))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Guardado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Acción guardada",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Button(
            onClick = viewModel::onGuardar,
            enabled = !estado.guardando && !mostrarConfirmacion,
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
            enabled = !estado.guardando && !mostrarConfirmacion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}

/** Texto que ve la persona para cada tipo de acción. */
private fun TipoAccion.etiqueta(): String = when (this) {
    TipoAccion.REVISION_VENTILACION -> "Revisión de ventilación"
    TipoAccion.AJUSTE_DE_EQUIPOS -> "Ajuste de equipos"
    TipoAccion.AVISO_A_SUPERVISOR -> "Aviso a supervisor"
    TipoAccion.OTRA -> "Otra"
}