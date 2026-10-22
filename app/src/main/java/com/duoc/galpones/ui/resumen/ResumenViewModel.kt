package com.duoc.galpones.ui.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.repository.AlertaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AlertaCriticaUi(
    val id: Long,
    val titulo: String,
    val estado: String,
    val detalle: String,
    val fecha: String
)

data class ResumenUiState(
    val cargando: Boolean = true,
    val pendientes: Int = 0,
    val enAtencion: Int = 0,
    val resueltas: Int = 0,
    val criticas: List<AlertaCriticaUi> = emptyList()
)

class ResumenViewModel(
    repositorio: AlertaRepository
) : ViewModel() {

    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy · HH:mm", Locale("es", "CL"))

    val estado: StateFlow<ResumenUiState> = combine(
        repositorio.contarPorEstado(EstadoAlerta.PENDIENTE),
        repositorio.contarPorEstado(EstadoAlerta.EN_ATENCION),
        repositorio.contarPorEstado(EstadoAlerta.RESUELTA),
        repositorio.observarActivas()
    ) { pendientes, enAtencion, resueltas, activas ->
        ResumenUiState(
            cargando = false,
            pendientes = pendientes,
            enAtencion = enAtencion,
            resueltas = resueltas,
            criticas = activas
                .filter { it.nivel == EstadoGalpon.CRITICO }
                .map { alerta ->
                    AlertaCriticaUi(
                        id = alerta.id,
                        titulo = "Galpón ${alerta.galponId} · Crítico",
                        estado = when (alerta.estado) {
                            EstadoAlerta.PENDIENTE -> "Pendiente"
                            EstadoAlerta.EN_ATENCION -> "En atención"
                            EstadoAlerta.RESUELTA -> "Resuelta"
                        },
                        detalle = "Temperatura: ${"%.1f".format(Locale("es", "CL"), alerta.temperatura)} °C · Humedad: ${"%.0f".format(alerta.humedad)} %",
                        fecha = formatoFecha.format(Date(alerta.fechaHora))
                    )
                }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResumenUiState()
    )

    class Factory(
        private val repositorio: AlertaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ResumenViewModel(repositorio) as T
    }
}