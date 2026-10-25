package com.duoc.galpones.domain.validation

import com.duoc.galpones.domain.model.TipoAccion

/** Error por campo (null si está bien). esValido es true cuando ningún campo tiene error. */
data class ResultadoValidacionAccion(
    val errorAlerta: String? = null,
    val errorTipo: String? = null,
    val errorDescripcion: String? = null
) {
    val esValido: Boolean
        get() = errorAlerta == null && errorTipo == null && errorDescripcion == null
}

/** Reglas del formulario de acción. Sin Android: se puede probar con tests unitarios. */
object ValidadorAccion {

    /** Mínimo de caracteres para que la descripción de la acción diga algo útil. */
    const val LARGO_MINIMO_DESCRIPCION = 10

    fun validarAlerta(alertaId: Long?): String? =
        if (alertaId == null || alertaId <= 0L) "Selecciona una alerta" else null

    fun validarTipo(tipo: TipoAccion?): String? =
        if (tipo == null) "Elige un tipo de acción" else null

    fun validarDescripcion(descripcion: String): String? = when {
        descripcion.isBlank() -> "Describe la acción realizada"
        descripcion.trim().length < LARGO_MINIMO_DESCRIPCION ->
            "Escribe al menos $LARGO_MINIMO_DESCRIPCION caracteres"
        else -> null
    }

    /** Revisa todos los campos juntos, para mostrar todos los errores a la vez. */
    fun validar(alertaId: Long?, tipo: TipoAccion?, descripcion: String): ResultadoValidacionAccion =
        ResultadoValidacionAccion(
            errorAlerta = validarAlerta(alertaId),
            errorTipo = validarTipo(tipo),
            errorDescripcion = validarDescripcion(descripcion)
        )
}