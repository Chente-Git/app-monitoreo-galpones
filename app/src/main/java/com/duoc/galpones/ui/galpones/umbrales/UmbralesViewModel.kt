package com.duoc.galpones.ui.galpones.umbrales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.repository.GalponRepository
import com.duoc.galpones.domain.validation.ValidadorUmbrales
import com.duoc.galpones.ui.components.OpcionGalpon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario de umbrales: lo escrito, errores por campo y si está guardando. */
data class UmbralesUiState(
    val galpones: List<OpcionGalpon> = emptyList(),
    val galponId: Int? = null,
    val tempMin: String = "",
    val tempMax: String = "",
    val humMin: String = "",
    val humMax: String = "",
    val errorGalpon: String? = null,
    val errorTempMin: String? = null,
    val errorTempMax: String? = null,
    val errorHumMin: String? = null,
    val errorHumMax: String? = null,
    val errorGeneral: String? = null,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false
)

/** Maneja el formulario de umbrales: valida con ValidadorUmbrales y guarda en Room. */
class UmbralesViewModel(
    private val repositorio: GalponRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(UmbralesUiState())
    val estado: StateFlow<UmbralesUiState> = _estado.asStateFlow()

    /** Copia de la lista de galpones, para cargar sus umbrales al elegir uno. */
    private var galpones: List<Galpon> = emptyList()

    init {
        viewModelScope.launch {
            repositorio.observarGalpones().collect { lista ->
                galpones = lista
                _estado.update {
                    it.copy(galpones = lista.map { g -> OpcionGalpon(g.id, "${g.nombre} · ${g.granja}") })
                }
            }
        }
    }

    /** Al elegir un galpón se cargan sus umbrales actuales para editarlos. */
    fun onGalponChange(id: Int) {
        val galpon = galpones.firstOrNull { it.id == id }
        _estado.update {
            it.copy(
                galponId = id,
                tempMin = galpon?.let { g -> texto(g.tempMin) } ?: it.tempMin,
                tempMax = galpon?.let { g -> texto(g.tempMax) } ?: it.tempMax,
                humMin = galpon?.let { g -> texto(g.humMin) } ?: it.humMin,
                humMax = galpon?.let { g -> texto(g.humMax) } ?: it.humMax,
                errorGalpon = null,
                errorTempMin = null,
                errorTempMax = null,
                errorHumMin = null,
                errorHumMax = null,
                errorGeneral = null
            )
        }
    }

    /** Las funciones onXChange guardan lo escrito y borran el error de ese campo. */
    fun onTempMinChange(valor: String) {
        _estado.update { it.copy(tempMin = valor, errorTempMin = null, errorGeneral = null) }
    }

    fun onTempMaxChange(valor: String) {
        _estado.update { it.copy(tempMax = valor, errorTempMax = null, errorGeneral = null) }
    }

    fun onHumMinChange(valor: String) {
        _estado.update { it.copy(humMin = valor, errorHumMin = null, errorGeneral = null) }
    }

    fun onHumMaxChange(valor: String) {
        _estado.update { it.copy(humMax = valor, errorHumMax = null, errorGeneral = null) }
    }

    /** Valida y, si todo está bien, guarda. Si hay errores los muestra y no toca la base de datos. */
    fun onGuardar() {
        val actual = _estado.value
        // Ignora un segundo toque mientras ya se está guardando.
        if (actual.guardando) return

        val resultado = ValidadorUmbrales.validar(
            actual.galponId, actual.tempMin, actual.tempMax, actual.humMin, actual.humMax
        )
        val galponId = actual.galponId
        val tempMin = ValidadorUmbrales.parsear(actual.tempMin)
        val tempMax = ValidadorUmbrales.parsear(actual.tempMax)
        val humMin = ValidadorUmbrales.parsear(actual.humMin)
        val humMax = ValidadorUmbrales.parsear(actual.humMax)

        if (!resultado.esValido || galponId == null ||
            tempMin == null || tempMax == null || humMin == null || humMax == null
        ) {
            _estado.update {
                it.copy(
                    errorGalpon = resultado.errorGalpon,
                    errorTempMin = resultado.errorTempMin,
                    errorTempMax = resultado.errorTempMax,
                    errorHumMin = resultado.errorHumMin,
                    errorHumMax = resultado.errorHumMax
                )
            }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(guardando = true, errorGeneral = null) }
            try {
                repositorio.actualizarUmbrales(galponId, tempMin, tempMax, humMin, humMax)
                _estado.update { it.copy(guardando = false, guardadoExitoso = true) }
            } catch (e: Exception) {
                _estado.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = "No se pudieron guardar los umbrales. Intenta de nuevo."
                    )
                }
            }
        }
    }

    /**
     * Se llama después de cerrar el formulario: limpia los campos editados
     * para que al reabrir no aparezcan los valores de la edición anterior.
     * Mantiene la lista de galpones, que viene del repositorio.
     */
    fun guardadoProcesado() {
        _estado.update {
            it.copy(
                guardadoExitoso = false,
                galponId = null,
                tempMin = "",
                tempMax = "",
                humMin = "",
                humMax = "",
                errorGalpon = null,
                errorTempMin = null,
                errorTempMax = null,
                errorHumMin = null,
                errorHumMax = null,
                errorGeneral = null
            )
        }
    }

    /** Muestra 20 en vez de 20.0 cuando el valor no tiene decimales. */
    private fun texto(valor: Double): String =
        if (valor % 1.0 == 0.0) valor.toInt().toString() else valor.toString()

    /** Fábrica que entrega las dependencias al ViewModel, porque Android no le pasa parámetros solo. */
    class Factory(
        private val repositorio: GalponRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UmbralesViewModel(repositorio) as T
    }
}
