package com.duoc.galpones.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.galpones.data.fakeapi.FakeUsuariosApi
import com.duoc.galpones.domain.SesionActual
import com.duoc.galpones.domain.ValidadorLogin
import com.duoc.galpones.domain.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val perfil: Rol? = null,
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorPerfil: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val ingresoExitoso: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errorCorreo = null, errorGeneral = null) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errorClave = null, errorGeneral = null) }
    }

    fun onPerfilChange(valor: Rol) {
        _estado.update { it.copy(perfil = valor, errorPerfil = null, errorGeneral = null) }
    }

    fun onIngresar() {
        val actual = _estado.value
        if (actual.cargando) return

        val resultado = ValidadorLogin.validar(actual.correo, actual.clave, actual.perfil)
        if (!resultado.esValido) {
            _estado.update {
                it.copy(
                    errorCorreo = resultado.errorCorreo,
                    errorClave = resultado.errorClave,
                    errorPerfil = resultado.errorPerfil
                )
            }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, errorGeneral = null) }
            val usuarios = FakeUsuariosApi.obtenerUsuarios()
            val usuario = usuarios.firstOrNull {
                it.correo.equals(actual.correo.trim(), ignoreCase = true) &&
                        it.clave == actual.clave &&
                        it.rol == actual.perfil
            }
            if (usuario == null) {
                _estado.update {
                    it.copy(
                        cargando = false,
                        errorGeneral = "Correo, clave o perfil incorrectos"
                    )
                }
            } else {
                SesionActual.iniciar(usuario)
                _estado.update { it.copy(cargando = false, ingresoExitoso = true) }
            }
        }
    }

    fun ingresoProcesado() {
        _estado.update { it.copy(ingresoExitoso = false) }
    }
}