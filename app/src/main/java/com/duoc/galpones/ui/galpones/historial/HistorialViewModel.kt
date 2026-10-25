package com.duoc.galpones.ui.galpones.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.CalculadorEstado
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.repository.GalponRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Opción del filtro de galpones (un chip) en el historial. */
data class FiltroGalpon(val id: Int, val nombre: String)

/** Medición lista para mostrar, con textos formateados y su estado calculado. */
data class MedicionUi(
    val id: Long,
    val galpon: String,
    val temperatura: String,
    val humedad: String,
    val fechaHora: String,
    val estado: EstadoGalpon
)

/** Estado de Historial: filtros, galpón elegido (null = todos) y mediciones. */
data class HistorialUiState(
    val cargando: Boolean = true,
    val filtros: List<FiltroGalpon> = emptyList(),
    val seleccionado: Int? = null,
    val mediciones: List<MedicionUi> = emptyList()
)

/** Entrega el historial de mediciones y lo filtra por galpón según el chip elegido. */
@OptIn(ExperimentalCoroutinesApi::class)
class HistorialViewModel(
    private val repositorio: GalponRepository
) : ViewModel() {

    private val local = Locale("es", "CL")
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy · HH:mm", local)

    /** Galpón elegido en el filtro. Null significa todos. */
    private val seleccionado = MutableStateFlow<Int?>(null)

    val estado: StateFlow<HistorialUiState> = combine(
        repositorio.observarGalpones(),
        // Al cambiar el filtro se deja de escuchar la consulta anterior y se escucha la nueva.
        seleccionado.flatMapLatest { id -> repositorio.observarMediciones(id) },
        seleccionado
    ) { galpones, mediciones, id ->
        HistorialUiState(
            cargando = false,
            filtros = galpones.map { FiltroGalpon(it.id, it.nombre) },
            seleccionado = id,
            mediciones = mediciones.map { medicion ->
                val galpon = galpones.firstOrNull { it.id == medicion.galponId }
                MedicionUi(
                    id = medicion.id,
                    galpon = galpon?.nombre ?: "Galpón ${medicion.galponId}",
                    temperatura = String.format(local, "%.1f °C", medicion.temperatura),
                    humedad = String.format(local, "%.0f %%", medicion.humedad),
                    fechaHora = formatoFecha.format(Date(medicion.fechaHora)),
                    estado = if (galpon != null) {
                        CalculadorEstado.calcular(medicion, galpon)
                    } else {
                        EstadoGalpon.NORMAL
                    }
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        // Sigue escuchando Room 5 s sin pantalla (por ejemplo al girar el teléfono) y luego se detiene.
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistorialUiState()
    )

    /** Con null muestra las mediciones de todos los galpones. */
    fun seleccionar(galponId: Int?) {
        seleccionado.value = galponId
    }

    /** Fábrica que entrega las dependencias al ViewModel, porque Android no le pasa parámetros solo. */
    class Factory(
        private val repositorio: GalponRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HistorialViewModel(repositorio) as T
    }
}
