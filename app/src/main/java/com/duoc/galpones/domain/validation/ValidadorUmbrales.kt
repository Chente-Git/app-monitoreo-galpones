package com.duoc.galpones.domain.validation

data class ResultadoValidacionUmbrales(
    val errorGalpon: String? = null,
    val errorTempMin: String? = null,
    val errorTempMax: String? = null,
    val errorHumMin: String? = null,
    val errorHumMax: String? = null
) {
    val esValido: Boolean
        get() = errorGalpon == null && errorTempMin == null && errorTempMax == null &&
                errorHumMin == null && errorHumMax == null
}

object ValidadorUmbrales {

    const val TEMP_MINIMA = 0.0
    const val TEMP_MAXIMA = 50.0
    const val HUM_MINIMA = 0.0
    const val HUM_MAXIMA = 100.0

    /** Acepta coma o punto decimal. Devuelve null si no es un número finito. */
    fun parsear(texto: String): Double? {
        val numero = texto.trim().replace(',', '.').toDoubleOrNull() ?: return null
        return if (numero.isFinite()) numero else null
    }

    fun validarGalpon(galponId: Int?): String? =
        if (galponId == null) "Selecciona un galpón" else null

    private fun validarValor(
        texto: String,
        descripcion: String,
        minimo: Double,
        maximo: Double,
        unidad: String
    ): String? {
        if (texto.isBlank()) return "Ingresa $descripcion"
        val valor = parsear(texto) ?: return "Ingresa un número válido"
        if (valor < minimo || valor > maximo) {
            return "Debe estar entre ${minimo.toInt()} y ${maximo.toInt()} $unidad"
        }
        return null
    }

    /** El error de orden se muestra en el campo máximo, y solo si el mínimo es válido. */
    private fun validarOrden(errorMinimo: String?, minimo: Double?, maximo: Double?): String? =
        if (errorMinimo == null && minimo != null && maximo != null && minimo >= maximo) {
            "El máximo debe ser mayor que el mínimo"
        } else {
            null
        }

    fun validar(
        galponId: Int?,
        tempMin: String,
        tempMax: String,
        humMin: String,
        humMax: String
    ): ResultadoValidacionUmbrales {
        val errorTempMin = validarValor(
            tempMin, "la temperatura mínima", TEMP_MINIMA, TEMP_MAXIMA, "°C"
        )
        val errorTempMax = validarValor(
            tempMax, "la temperatura máxima", TEMP_MINIMA, TEMP_MAXIMA, "°C"
        ) ?: validarOrden(errorTempMin, parsear(tempMin), parsear(tempMax))

        val errorHumMin = validarValor(
            humMin, "la humedad mínima", HUM_MINIMA, HUM_MAXIMA, "%"
        )
        val errorHumMax = validarValor(
            humMax, "la humedad máxima", HUM_MINIMA, HUM_MAXIMA, "%"
        ) ?: validarOrden(errorHumMin, parsear(humMin), parsear(humMax))

        return ResultadoValidacionUmbrales(
            errorGalpon = validarGalpon(galponId),
            errorTempMin = errorTempMin,
            errorTempMax = errorTempMax,
            errorHumMin = errorHumMin,
            errorHumMax = errorHumMax
        )
    }
}
