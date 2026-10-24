package com.duoc.galpones.domain.validation

data class ResultadoValidacionObservacion(
    val errorGalpon: String? = null,
    val errorDescripcion: String? = null
) {
    val esValido: Boolean
        get() = errorGalpon == null && errorDescripcion == null
}

object ValidadorObservacion {

    const val LARGO_MINIMO = 10
    const val LARGO_MAXIMO = 200

    fun validarGalpon(galponId: Int?): String? =
        if (galponId == null) "Selecciona un galpón" else null

    fun validarDescripcion(descripcion: String): String? = when {
        descripcion.isBlank() -> "Describe lo que observaste"
        descripcion.trim().length < LARGO_MINIMO -> "Escribe al menos $LARGO_MINIMO caracteres"
        descripcion.trim().length > LARGO_MAXIMO -> "Escribe máximo $LARGO_MAXIMO caracteres"
        else -> null
    }

    fun validar(galponId: Int?, descripcion: String): ResultadoValidacionObservacion =
        ResultadoValidacionObservacion(
            errorGalpon = validarGalpon(galponId),
            errorDescripcion = validarDescripcion(descripcion)
        )
}
