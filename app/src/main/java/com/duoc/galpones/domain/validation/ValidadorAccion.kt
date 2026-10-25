package com.duoc.galpones.domain.validation

import com.duoc.galpones.domain.model.TipoAccion

data class ResultadoValidacionAccion(
    val errorAlerta: String? = null,
    val errorTipo: String? = null,
    val errorDescripcion: String? = null
) {
    val esValido: Boolean
        get() = errorAlerta == null && errorTipo == null && errorDescripcion == null
}

object ValidadorAccion {

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

    fun validar(alertaId: Long?, tipo: TipoAccion?, descripcion: String): ResultadoValidacionAccion =
        ResultadoValidacionAccion(
            errorAlerta = validarAlerta(alertaId),
            errorTipo = validarTipo(tipo),
            errorDescripcion = validarDescripcion(descripcion)
        )
}