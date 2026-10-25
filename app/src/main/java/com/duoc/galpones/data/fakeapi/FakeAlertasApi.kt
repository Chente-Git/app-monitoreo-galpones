package com.duoc.galpones.data.fakeapi

import com.duoc.galpones.domain.model.Alerta
import com.duoc.galpones.domain.model.EstadoAlerta
import com.duoc.galpones.domain.model.EstadoGalpon
import kotlinx.coroutines.delay

/** API simulada: devuelve alertas fijas con un retardo, como si viniera de un servidor. */
object FakeAlertasApi {

    /** Siete alertas de ejemplo con distintos niveles y estados, para probar la lista y el resumen. */
    suspend fun obtenerAlertas(): List<Alerta> {
        // El retardo simula la espera de una respuesta de red.
        delay(1200)
        val ahora = System.currentTimeMillis()
        val minuto = 60_000L
        val hora = 60 * minuto
        return listOf(
            Alerta(galponId = 1, nivel = EstadoGalpon.CRITICO, temperatura = 38.2, humedad = 85.0,
                fechaHora = ahora - 10 * minuto, estado = EstadoAlerta.PENDIENTE),
            Alerta(galponId = 3, nivel = EstadoGalpon.CRITICO, temperatura = 9.5, humedad = 48.0,
                fechaHora = ahora - 45 * minuto, estado = EstadoAlerta.EN_ATENCION),
            Alerta(galponId = 2, nivel = EstadoGalpon.ADVERTENCIA, temperatura = 33.4, humedad = 74.0,
                fechaHora = ahora - 2 * hora, estado = EstadoAlerta.PENDIENTE),
            Alerta(galponId = 4, nivel = EstadoGalpon.ADVERTENCIA, temperatura = 14.0, humedad = 62.0,
                fechaHora = ahora - 3 * hora, estado = EstadoAlerta.PENDIENTE),
            Alerta(galponId = 1, nivel = EstadoGalpon.ADVERTENCIA, temperatura = 32.8, humedad = 78.0,
                fechaHora = ahora - 5 * hora, estado = EstadoAlerta.RESUELTA),
            Alerta(galponId = 2, nivel = EstadoGalpon.CRITICO, temperatura = 37.1, humedad = 90.0,
                fechaHora = ahora - 8 * hora, estado = EstadoAlerta.PENDIENTE),
            Alerta(galponId = 3, nivel = EstadoGalpon.ADVERTENCIA, temperatura = 15.2, humedad = 55.0,
                fechaHora = ahora - 26 * hora, estado = EstadoAlerta.RESUELTA)
        )
    }
}