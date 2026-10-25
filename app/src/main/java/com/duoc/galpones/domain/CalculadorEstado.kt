package com.duoc.galpones.domain

import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion

/**
 * Calcula el estado de un galpón (normal, advertencia o crítico) a partir de
 * una medición y los umbrales configurados. El margen de advertencia marca
 * la banda cercana al límite donde el galpón aún no es crítico pero conviene
 * revisarlo: una medición dentro del 10% de los extremos del rango.
 */
object CalculadorEstado {

    /** Porcentaje del rango total que se considera zona de advertencia. */
    const val MARGEN_ADVERTENCIA = 0.10

    /** Devuelve el peor estado entre temperatura y humedad. Lo usan Inicio, Detalle e Historial. */
    fun calcular(medicion: Medicion, galpon: Galpon): EstadoGalpon {
        val estadoTemperatura = estadoDeValor(medicion.temperatura, galpon.tempMin, galpon.tempMax)
        val estadoHumedad = estadoDeValor(medicion.humedad, galpon.humMin, galpon.humMax)
        // El enum va de menos a más grave, por eso maxOf entrega el peor de los dos estados.
        return maxOf(estadoTemperatura, estadoHumedad)
    }

    /** Fuera del rango es crítico; cerca de un borde (dentro del margen) es advertencia. */
    private fun estadoDeValor(valor: Double, min: Double, max: Double): EstadoGalpon {
        if (valor < min || valor > max) return EstadoGalpon.CRITICO

        val margen = (max - min) * MARGEN_ADVERTENCIA
        val cercaDelBorde = valor < min + margen || valor > max - margen
        return if (cercaDelBorde) EstadoGalpon.ADVERTENCIA else EstadoGalpon.NORMAL
    }
}