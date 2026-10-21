package com.duoc.galpones.ui.alertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.repository.AlertaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Alerta lista para mostrar: textos ya formateados, la pantalla solo los dibuja. */
data class AlertaUi(
    val id: Long,
    val galpon: String,
    val nivel: EstadoGalpon,
    val nivelTexto: String,
    val estadoTexto: String,
    val temperatura: String,
    val humedad: String,
    val fecha: String,
    val hora: String
)

data class AlertasUiState(
    val cargando: Boolean = true,
    val alertas: List<AlertaUi> = emptyList()
)

class AlertasViewModel(
    private val repositorio: AlertaRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(AlertasUiState())
    val estado: StateFlow<AlertasUiState> = _estado.asStateFlow()

    private val local = Locale("es", "CL")
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy", local)
    private val formatoHora = SimpleDateFormat("HH:mm", local)

    init {
        viewModelScope.launch {
            repositorio.cargarInicialesSiVacio()
            repositorio.observarActivas().collect { lista ->
                _estado.update { it.copy(cargando = false, alertas = lista.map(::aUi)) }
            }
        }
    }

    private fun aUi(alerta: Alerta): AlertaUi {
        val fecha = Date(alerta.fechaHora)
        return AlertaUi(
            id = alerta.id,
            galpon = "Galpón ${alerta.galponId}",
            nivel = alerta.nivel,
            nivelTexto = when (alerta.nivel) {
                EstadoGalpon.CRITICO -> "Crítico"
                EstadoGalpon.ADVERTENCIA -> "Advertencia"
                EstadoGalpon.NORMAL -> "Normal"
            },
            estadoTexto = when (alerta.estado) {
                EstadoAlerta.PENDIENTE -> "Pendiente"
                EstadoAlerta.EN_ATENCION -> "En atención"
                EstadoAlerta.RESUELTA -> "Resuelta"
            },
            temperatura = String.format(local, "%.1f °C", alerta.temperatura),
            humedad = String.format(local, "%.0f %%", alerta.humedad),
            fecha = formatoFecha.format(fecha),
            hora = formatoHora.format(fecha)
        )
    }

    class Factory(
        private val repositorio: AlertaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AlertasViewModel(repositorio) as T
    }
}