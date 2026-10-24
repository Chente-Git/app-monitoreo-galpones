package com.duoc.galpones.ui.galpones.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.CalculadorEstado
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.repository.GalponRepository
import com.duoc.galpones.domain.repository.ObservacionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ObservacionUi(
    val id: Long,
    val texto: String,
    val fecha: String
)

data class DetalleUiState(
    val cargando: Boolean = true,
    val encontrado: Boolean = true,
    val nombre: String = "",
    val granja: String = "",
    val estado: EstadoGalpon = EstadoGalpon.NORMAL,
    val temperatura: String = "--",
    val humedad: String = "--",
    val actualizacion: String = "sin datos",
    val umbralTemperatura: String = "",
    val umbralHumedad: String = "",
    val observaciones: List<ObservacionUi> = emptyList()
)

class DetalleViewModel(
    galponId: Int,
    galponRepositorio: GalponRepository,
    observacionRepositorio: ObservacionRepository
) : ViewModel() {

    private val local = Locale("es", "CL")
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy · HH:mm", local)

    val estado: StateFlow<DetalleUiState> = combine(
        galponRepositorio.observarGalpon(galponId),
        galponRepositorio.observarUltimaMedicion(galponId),
        observacionRepositorio.observarPorGalpon(galponId)
    ) { galpon, medicion, observaciones ->
        if (galpon == null) {
            DetalleUiState(cargando = false, encontrado = false)
        } else {
            DetalleUiState(
                cargando = false,
                nombre = galpon.nombre,
                granja = galpon.granja,
                estado = if (medicion != null) {
                    CalculadorEstado.calcular(medicion, galpon)
                } else {
                    EstadoGalpon.NORMAL
                },
                temperatura = medicion?.let { String.format(local, "%.1f °C", it.temperatura) } ?: "--",
                humedad = medicion?.let { String.format(local, "%.0f %%", it.humedad) } ?: "--",
                actualizacion = medicion?.let { formatoFecha.format(Date(it.fechaHora)) } ?: "sin datos",
                umbralTemperatura = String.format(local, "%.1f a %.1f °C", galpon.tempMin, galpon.tempMax),
                umbralHumedad = String.format(local, "%.0f a %.0f %%", galpon.humMin, galpon.humMax),
                observaciones = observaciones.map {
                    ObservacionUi(
                        id = it.id,
                        texto = it.descripcion,
                        fecha = formatoFecha.format(Date(it.fechaHora))
                    )
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetalleUiState()
    )

    class Factory(
        private val galponId: Int,
        private val galponRepositorio: GalponRepository,
        private val observacionRepositorio: ObservacionRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DetalleViewModel(galponId, galponRepositorio, observacionRepositorio) as T
    }
}
