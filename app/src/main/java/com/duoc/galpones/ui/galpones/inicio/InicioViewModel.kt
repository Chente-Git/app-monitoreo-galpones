package com.duoc.galpones.ui.galpones.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.CalculadorEstado
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion
import com.duoc.galpones.domain.repository.GalponRepository
import com.duoc.galpones.platform.notificaciones.NotificadorCritico
import com.duoc.galpones.platform.vibracion.Vibrador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Galpón listo para mostrar: textos ya formateados, la pantalla solo los dibuja. */
data class GalponUi(
    val id: Int,
    val nombre: String,
    val granja: String,
    val temperatura: String,
    val humedad: String,
    val actualizacion: String,
    val estado: EstadoGalpon
)

/** Estado de la pantalla Inicio: si está cargando y la lista de galpones. */
data class InicioUiState(
    val cargando: Boolean = true,
    val galpones: List<GalponUi> = emptyList()
)

/** Prepara la lista de Inicio y avisa (notificación y vibración) cuando un galpón entra en crítico. */
class InicioViewModel(
    private val repositorio: GalponRepository,
    private val notificador: NotificadorCritico,
    private val vibrador: Vibrador
) : ViewModel() {

    private val _estado = MutableStateFlow(InicioUiState())
    val estado: StateFlow<InicioUiState> = _estado.asStateFlow()

    private val local = Locale("es", "CL")
    private val formatoFecha = SimpleDateFormat("dd-MM-yyyy · HH:mm", local)

    /** Última lista calculada, para reintentar los avisos al conceder el permiso. */
    private var ultimaLista: List<GalponUi> = emptyList()

    init {
        viewModelScope.launch {
            repositorio.cargarInicialesSiVacio()
            // Une galpones y últimas mediciones: cada cambio en Room recalcula la lista.
            combine(
                repositorio.observarGalpones(),
                repositorio.observarUltimasMediciones()
            ) { galpones, mediciones ->
                galpones.map { galpon ->
                    aUi(galpon, mediciones.firstOrNull { it.galponId == galpon.id })
                }
            }.collect { lista ->
                _estado.update { it.copy(cargando = false, galpones = lista) }
                avisarCriticos(lista)
            }
        }
    }

    /** Se llama al conceder el permiso de notificaciones, para avisar de lo que ya estaba crítico. */
    fun reintentarAvisos() {
        avisarCriticos(ultimaLista)
    }

    /** Convierte galpón y medición en textos listos. Sin medición muestra guiones y estado normal. */
    private fun aUi(galpon: Galpon, medicion: Medicion?): GalponUi {
        val estado = if (medicion != null) {
            CalculadorEstado.calcular(medicion, galpon)
        } else {
            EstadoGalpon.NORMAL
        }
        return GalponUi(
            id = galpon.id,
            nombre = galpon.nombre,
            granja = galpon.granja,
            temperatura = medicion?.let { String.format(local, "%.1f °C", it.temperatura) } ?: "--",
            humedad = medicion?.let { String.format(local, "%.0f %%", it.humedad) } ?: "--",
            actualizacion = medicion?.let { formatoFecha.format(Date(it.fechaHora)) } ?: "sin datos",
            estado = estado
        )
    }

    /**
     * Notifica y hace vibrar solo cuando un galpón ENTRA en estado crítico.
     * Si deja de estar crítico y vuelve a estarlo, avisa de nuevo.
     */
    private fun avisarCriticos(lista: List<GalponUi>) {
        ultimaLista = lista
        val criticos = lista.filter { it.estado == EstadoGalpon.CRITICO }
        criticosAvisados.retainAll(criticos.map { it.id }.toSet())

        var huboAviso = false
        for (galpon in criticos) {
            if (galpon.id in criticosAvisados) continue
            val enviada = notificador.notificarCritico(
                galponId = galpon.id,
                nombre = galpon.nombre,
                temperatura = galpon.temperatura,
                humedad = galpon.humedad
            )
            if (enviada) {
                criticosAvisados.add(galpon.id)
                huboAviso = true
            }
        }
        if (huboAviso) vibrador.vibrarAlerta()
    }

    /** Fábrica que entrega las dependencias al ViewModel, porque Android no le pasa parámetros solo. */
    class Factory(
        private val repositorio: GalponRepository,
        private val notificador: NotificadorCritico,
        private val vibrador: Vibrador
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            InicioViewModel(repositorio, notificador, vibrador) as T
    }

    private companion object {
        /** Galpones críticos ya avisados. Vive con el proceso, no con el ViewModel. */
        val criticosAvisados = mutableSetOf<Int>()
    }
}
