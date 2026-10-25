package com.duoc.galpones.ui.accion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.SesionActual
import com.duoc.galpones.domain.validation.ValidadorAccion
import com.duoc.galpones.domain.model.Accion
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.TipoAccion
import com.duoc.galpones.domain.repository.AccionRepository
import com.duoc.galpones.domain.repository.AlertaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario de acción: alerta, tipo, descripción y errores por campo. */
data class AccionUiState(
    val alertaId: Long? = null,
    val resumenAlerta: String = "",
    val tipo: TipoAccion? = null,
    val descripcion: String = "",
    val errorAlerta: String? = null,
    val errorTipo: String? = null,
    val errorDescripcion: String? = null,
    val errorGeneral: String? = null,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)

/** Valida y guarda una acción sobre una alerta, y deja la alerta en atención. */
class AccionViewModel(
    private val alertaIdInicial: Long,
    private val alertaRepository: AlertaRepository,
    private val accionRepository: AccionRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(AccionUiState())
    val estado: StateFlow<AccionUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            // Carga la alerta para mostrar su resumen. Si no existe, muestra un error.
            val alerta = alertaRepository.obtenerPorId(alertaIdInicial)
            if (alerta == null) {
                _estado.update { it.copy(errorAlerta = "No se encontró la alerta") }
            } else {
                val nivel = when (alerta.nivel) {
                    EstadoGalpon.CRITICO -> "Crítico"
                    EstadoGalpon.ADVERTENCIA -> "Advertencia"
                    EstadoGalpon.NORMAL -> "Normal"
                }
                _estado.update {
                    it.copy(
                        alertaId = alerta.id,
                        resumenAlerta = "Galpón ${alerta.galponId} · $nivel"
                    )
                }
            }
        }
    }

    fun onTipoChange(valor: TipoAccion) {
        _estado.update { it.copy(tipo = valor, errorTipo = null, errorGeneral = null) }
    }

    fun onDescripcionChange(valor: String) {
        _estado.update { it.copy(descripcion = valor, errorDescripcion = null, errorGeneral = null) }
    }

    /** Valida y, si todo está bien, guarda la acción. Si hay errores los muestra y no guarda. */
    fun onGuardar() {
        val actual = _estado.value
        // Ignora un segundo toque mientras ya se está guardando.
        if (actual.guardando) return

        val alertaId = actual.alertaId
        val tipo = actual.tipo
        val resultado = ValidadorAccion.validar(alertaId, tipo, actual.descripcion)
        if (!resultado.esValido || alertaId == null || tipo == null) {
            _estado.update {
                it.copy(
                    errorAlerta = resultado.errorAlerta,
                    errorTipo = resultado.errorTipo,
                    errorDescripcion = resultado.errorDescripcion
                )
            }
            return
        }

        // La acción queda a nombre de quien inició sesión.
        val usuario = SesionActual.usuario
        if (usuario == null) {
            _estado.update { it.copy(errorGeneral = "Inicia sesión para registrar una acción") }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorGeneral = null) }
            try {
                accionRepository.registrar(
                    Accion(
                        alertaId = alertaId,
                        usuarioId = usuario.id,
                        tipo = tipo,
                        descripcion = actual.descripcion.trim(),
                        fechaHora = System.currentTimeMillis()
                    )
                )
                // Registrar una acción pasa la alerta a EN_ATENCION.
                alertaRepository.actualizarEstado(alertaId, EstadoAlerta.EN_ATENCION)
                _estado.update { it.copy(guardando = false, guardadoExitoso = true) }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = "No se pudo guardar la acción. Intenta de nuevo."
                    )
                }
            }
        }
    }

    /** Apaga la señal de guardado para que la pantalla no se cierre sola otra vez. */
    fun guardadoProcesado() {
        _estado.update { it.copy(guardadoExitoso = false) }
    }

    /** Fábrica que entrega las dependencias al ViewModel, porque Android no le pasa parámetros solo. */
    class Factory(
        private val alertaId: Long,
        private val alertaRepository: AlertaRepository,
        private val accionRepository: AccionRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AccionViewModel(alertaId, alertaRepository, accionRepository) as T
    }
}