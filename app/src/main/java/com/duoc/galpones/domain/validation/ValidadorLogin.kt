package com.duoc.galpones.domain.validation

import com.duoc.galpones.domain.model.Rol

/** Error por campo (null si está bien). esValido es true cuando ningún campo tiene error. */
data class ResultadoValidacionLogin(
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorPerfil: String? = null
) {
    val esValido: Boolean
        get() = errorCorreo == null && errorClave == null && errorPerfil == null
}

/** Reglas del formulario de login. Sin Android: se puede probar con tests unitarios. */
object ValidadorLogin {

    const val LARGO_MINIMO_CLAVE = 8

    /** Exige la forma nombre@dominio.ext. */
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "Ingresa tu correo"
        !formatoCorreo.matches(correo.trim()) -> "Ingresa un correo válido, por ejemplo nombre@galpones.cl"
        else -> null
    }

    fun validarClave(clave: String): String? = when {
        clave.isBlank() -> "Ingresa tu clave"
        clave.length < LARGO_MINIMO_CLAVE -> "La clave debe tener al menos $LARGO_MINIMO_CLAVE caracteres"
        else -> null
    }

    fun validarPerfil(perfil: Rol?): String? =
        if (perfil == null) "Elige un perfil" else null

    /** Revisa todos los campos juntos, para mostrar todos los errores a la vez. */
    fun validar(correo: String, clave: String, perfil: Rol?): ResultadoValidacionLogin =
        ResultadoValidacionLogin(
            errorCorreo = validarCorreo(correo),
            errorClave = validarClave(clave),
            errorPerfil = validarPerfil(perfil)
        )
}