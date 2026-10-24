package com.duoc.galpones.platform.vibracion

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

/** Vibración corta y repetida que acompaña a la notificación de estado crítico. */
class Vibrador(context: Context) {

    private val vibrador: Vibrator? = context.getSystemService(Vibrator::class.java)

    fun vibrarAlerta() {
        val servicio = vibrador ?: return
        if (!servicio.hasVibrator()) return
        // Patrón: espera 0 ms, vibra 400, pausa 200, vibra 400. El -1 evita que se repita.
        servicio.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1))
    }
}
