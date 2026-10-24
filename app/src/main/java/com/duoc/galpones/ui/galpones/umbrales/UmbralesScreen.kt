package com.duoc.galpones.ui.galpones.umbrales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.ui.components.CampoFormulario
import com.duoc.galpones.ui.components.ConfirmacionGuardado
import com.duoc.galpones.ui.components.MensajeErrorAnimado
import com.duoc.galpones.ui.components.SelectorGalpon
import com.duoc.galpones.ui.components.contenedorApp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmbralesScreen(
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UmbralesViewModel = viewModel(
        factory = UmbralesViewModel.Factory(contenedorApp().galponRepository)
    )
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            delay(1800)
            viewModel.guardadoProcesado()
            onGuardado()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Umbrales") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Define los rangos aceptables. Fuera de ellos el galpón pasa a estado crítico.")

            SelectorGalpon(
                opciones = estado.galpones,
                seleccionado = estado.galponId,
                onSeleccionar = viewModel::onGalponChange,
                error = estado.errorGalpon
            )

            CampoFormulario(
                valor = estado.tempMin,
                onValorChange = viewModel::onTempMinChange,
                etiqueta = "Temperatura mínima (°C)",
                icono = Icons.Filled.Thermostat,
                error = estado.errorTempMin,
                ayuda = "Entre 0 y 50",
                tipoTeclado = KeyboardType.Decimal
            )
            CampoFormulario(
                valor = estado.tempMax,
                onValorChange = viewModel::onTempMaxChange,
                etiqueta = "Temperatura máxima (°C)",
                icono = Icons.Filled.Thermostat,
                error = estado.errorTempMax,
                ayuda = "Entre 0 y 50",
                tipoTeclado = KeyboardType.Decimal
            )
            CampoFormulario(
                valor = estado.humMin,
                onValorChange = viewModel::onHumMinChange,
                etiqueta = "Humedad mínima (%)",
                icono = Icons.Filled.WaterDrop,
                error = estado.errorHumMin,
                ayuda = "Entre 0 y 100",
                tipoTeclado = KeyboardType.Decimal
            )
            CampoFormulario(
                valor = estado.humMax,
                onValorChange = viewModel::onHumMaxChange,
                etiqueta = "Humedad máxima (%)",
                icono = Icons.Filled.WaterDrop,
                error = estado.errorHumMax,
                ayuda = "Entre 0 y 100",
                tipoTeclado = KeyboardType.Decimal
            )

            MensajeErrorAnimado(estado.errorGeneral)
            ConfirmacionGuardado(
                visible = estado.guardadoExitoso,
                texto = "Umbrales guardados"
            )

            Button(
                onClick = viewModel::onGuardar,
                enabled = !estado.guardando && !estado.guardadoExitoso,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar umbrales")
                }
            }
            OutlinedButton(
                onClick = onCancelar,
                enabled = !estado.guardando && !estado.guardadoExitoso,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
