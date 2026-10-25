package com.duoc.galpones.ui.login

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.galpones.domain.model.Rol
import com.duoc.galpones.ui.components.CampoFormulario
import com.duoc.galpones.ui.components.MensajeErrorAnimado

/** Pantalla de inicio de sesión: correo, clave y perfil. Si todo es válido pasa a Inicio. */
@Composable
fun LoginScreen(
    onIngresoExitoso: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    // ingresoProcesado evita que el ingreso se repita si la pantalla se redibuja.
    LaunchedEffect(estado.ingresoExitoso) {
        if (estado.ingresoExitoso) {
            viewModel.ingresoProcesado()
            onIngresoExitoso()
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
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Monitoreo de galpones",
            style = MaterialTheme.typography.bodyMedium
        )

        CampoFormulario(
            valor = estado.correo,
            onValorChange = viewModel::onCorreoChange,
            etiqueta = "Correo",
            icono = Icons.Filled.Email,
            error = estado.errorCorreo,
            tipoTeclado = KeyboardType.Email
        )

        CampoFormulario(
            valor = estado.clave,
            onValorChange = viewModel::onClaveChange,
            etiqueta = "Clave",
            icono = Icons.Filled.Lock,
            error = estado.errorClave,
            tipoTeclado = KeyboardType.Password,
            ocultarTexto = true
        )

        Text(
            text = "Perfil",
            style = MaterialTheme.typography.titleMedium
        )
        Rol.values().forEach { rol ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onPerfilChange(rol) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = estado.perfil == rol,
                    onClick = null
                )
                Text(
                    text = rol.etiqueta(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        MensajeErrorAnimado(estado.errorPerfil)
        MensajeErrorAnimado(estado.errorGeneral)

        Button(
            onClick = viewModel::onIngresar,
            enabled = !estado.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado.cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Entrar")
            }
        }
    }
}

/** Texto que ve la persona para cada perfil. */
private fun Rol.etiqueta(): String = when (this) {
    Rol.OPERARIO -> "Operario"
    Rol.SUPERVISOR -> "Supervisor"
    Rol.JEFATURA -> "Jefatura"
}