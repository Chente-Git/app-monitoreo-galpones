package com.duoc.galpones.domain.validation

/** Error por campo (null si está bien). esValido es true cuando ningún campo tiene error. */
data class ResultadoValidacionObservacion(
    val errorGalpon: String? = null,
    val errorDescripcion: String? = null
) {
    val esValido: Boolean
        get() = errorGalpon == null && errorDescripcion == null
}

/** Reglas del formulario de observación. Sin Android: se puede probar con tests unitarios. */
object ValidadorObservacion {

    /** Largo permitido del texto, en caracteres, sin contar espacios al inicio ni al final. */
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

    /** Revisa todos los campos juntos, para mostrar todos los errores a la vez. */
    fun validar(galponId: Int?, descripcion: String): ResultadoValidacionObservacion =
        ResultadoValidacionObservacion(
            errorGalpon = validarGalpon(galponId),
            errorDescripcion = validarDescripcion(descripcion)
        )
}
