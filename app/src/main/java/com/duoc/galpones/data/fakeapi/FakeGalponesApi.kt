package com.duoc.galpones.data.fakeapi

import com.duoc.galpones.domain.model.Galpon
import com.duoc.galpones.domain.model.Medicion
import kotlinx.coroutines.delay

/**
 * API simulada de galpones y mediciones. Los ids 1 a 4 coinciden con los de FakeAlertasApi
 * y los umbrales hacen coherentes los estados de esas alertas.
 */
object FakeGalponesApi {

    private const val INTERVALO_MEDICIONES = 30 * 60_000L

    suspend fun obtenerGalpones(): List<Galpon> {
        delay(1000)
        return listOf(
            Galpon(id = 1, granja = "Granja Norte", nombre = "Galpón 1",
                tempMin = 20.0, tempMax = 35.0, humMin = 50.0, humMax = 80.0),
            Galpon(id = 2, granja = "Granja Norte", nombre = "Galpón 2",
                tempMin = 20.0, tempMax = 35.0, humMin = 45.0, humMax = 75.0),
            Galpon(id = 3, granja = "Granja Sur", nombre = "Galpón 3",
                tempMin = 14.0, tempMax = 30.0, humMin = 40.0, humMax = 70.0),
            Galpon(id = 4, granja = "Granja Sur", nombre = "Galpón 4",
                tempMin = 13.0, tempMax = 28.0, humMin = 40.0, humMax = 75.0)
        )
    }

    /** Seis mediciones por galpón, cada 30 minutos. La primera de cada lista es la más reciente. */
    suspend fun obtenerMediciones(): List<Medicion> {
        delay(1200)
        val ahora = System.currentTimeMillis()
        val series: Map<Int, List<Pair<Double, Double>>> = mapOf(
            1 to listOf(38.2 to 85.0, 36.5 to 82.0, 33.0 to 78.0, 31.2 to 72.0, 29.8 to 68.0, 28.5 to 65.0),
            2 to listOf(33.4 to 74.0, 32.9 to 72.0, 31.0 to 70.0, 29.5 to 66.0, 28.0 to 63.0, 27.2 to 60.0),
            3 to listOf(9.5 to 48.0, 11.8 to 50.0, 15.2 to 55.0, 17.0 to 57.0, 19.5 to 60.0, 20.8 to 62.0),
            4 to listOf(21.0 to 58.0, 20.5 to 57.0, 20.0 to 59.0, 19.8 to 60.0, 20.2 to 58.0, 21.5 to 56.0)
        )
        return series.flatMap { (galponId, valores) ->
            valores.mapIndexed { indice, (temperatura, humedad) ->
                Medicion(
                    galponId = galponId,
                    temperatura = temperatura,
                    humedad = humedad,
                    fechaHora = ahora - indice * INTERVALO_MEDICIONES
                )
            }
        }
    }
}
