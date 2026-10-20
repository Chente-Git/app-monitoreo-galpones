package com.duoc.galpones.domain

import com.duoc.galpones.domain.model.EstadoGalpon
import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion

object CalculadorEstado {

    const val MARGEN_ADVERTENCIA = 0.10

    fun calcular(medicion: Medicion, galpon: Galpon): EstadoGalpon {
        val estadoTemperatura = estadoDeValor(medicion.temperatura, galpon.tempMin, galpon.tempMax)
        val estadoHumedad = estadoDeValor(medicion.humedad, galpon.humMin, galpon.humMax)
        return maxOf(estadoTemperatura, estadoHumedad)
    }

    private fun estadoDeValor(valor: Double, min: Double, max: Double): EstadoGalpon {
        if (valor < min || valor > max) return EstadoGalpon.CRITICO

        val margen = (max - min) * MARGEN_ADVERTENCIA
        val cercaDelBorde = valor < min + margen || valor > max - margen
        return if (cercaDelBorde) EstadoGalpon.ADVERTENCIA else EstadoGalpon.NORMAL
    }
}