package com.duoc.galpones.ui.galpones.observacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.model.Observacion
import com.duoc.galpones.domain.repository.GalponRepository
import com.duoc.galpones.domain.repository.ObservacionRepository
import com.duoc.galpones.domain.validation.ValidadorObservacion
import com.duoc.galpones.ui.components.OpcionGalpon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ObservacionUiState(
    val galpones: List<OpcionGalpon> = emptyList(),
    val galponId: Int? = null,
    val descripcion: String = "",
    val errorGalpon: String? = null,
    val errorDescripcion: String? = null,
    val errorGeneral: String? = null,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)

class ObservacionViewModel(
    galponRepositorio: GalponRepository,
    private val observacionRepositorio: ObservacionRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(ObservacionUiState())
    val estado: StateFlow<ObservacionUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            galponRepositorio.observarGalpones().collect { lista ->
                _estado.update {
                    it.copy(galpones = lista.map { g -> OpcionGalpon(g.id, "${g.nombre} · ${g.granja}") })
                }
            }
        }
    }

    fun onGalponChange(id: Int) {
        _estado.update { it.copy(galponId = id, errorGalpon = null, errorGeneral = null) }
    }

    fun onDescripcionChange(valor: String) {
        _estado.update { it.copy(descripcion = valor, errorDescripcion = null, errorGeneral = null) }
    }

    fun onGuardar() {
        val actual = _estado.value
        if (actual.guardando) return

        val resultado = ValidadorObservacion.validar(actual.galponId, actual.descripcion)
        val galponId = actual.galponId
        if (!resultado.esValido || galponId == null) {
            _estado.update {
                it.copy(
                    errorGalpon = resultado.errorGalpon,
                    errorDescripcion = resultado.errorDescripcion
                )
            }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorGeneral = null) }
            try {
                observacionRepositorio.registrar(
                    Observacion(
                        galponId = galponId,
                        descripcion = actual.descripcion.trim(),
                        fechaHora = System.currentTimeMillis()
                    )
                )
                _estado.update { it.copy(guardando = false, guardadoExitoso = true) }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = "No se pudo guardar la observación. Intenta de nuevo."
                    )
                }
            }
        }
    }

    fun guardadoProcesado() {
        _estado.update { it.copy(guardadoExitoso = false) }
    }

    class Factory(
        private val galponRepositorio: GalponRepository,
        private val observacionRepositorio: ObservacionRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ObservacionViewModel(galponRepositorio, observacionRepositorio) as T
    }
}
