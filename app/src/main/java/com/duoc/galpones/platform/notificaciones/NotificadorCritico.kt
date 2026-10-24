package com.duoc.galpones.platform.notificaciones

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.duoc.galpones.MainActivity

/** Crea el canal de notificaciones y avisa cuando un galpón entra en estado crítico. */
class NotificadorCritico(private val context: Context) {

    init {
        crearCanal()
    }

    /** Falso si el usuario rechazó el permiso POST_NOTIFICATIONS o desactivó las notificaciones. */
    fun puedeNotificar(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    /** Devuelve true si la notificación se envió. */
    @SuppressLint("MissingPermission") // el permiso se comprueba en puedeNotificar()
    fun notificarCritico(
        galponId: Int,
        nombre: String,
        temperatura: String,
        humedad: String
    ): Boolean {
        if (!puedeNotificar()) return false

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendiente = PendingIntent.getActivity(
            context,
            galponId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("$nombre en estado crítico")
            .setContentText("Temperatura $temperatura · Humedad $humedad")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .build()

        NotificationManagerCompat.from(context).notify(ID_BASE + galponId, notificacion)
        return true
    }

    private fun crearCanal() {
        val canal = NotificationChannel(
            CANAL_ID,
            "Alertas críticas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisos cuando un galpón entra en estado crítico"
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(canal)
    }

    companion object {
        const val CANAL_ID = "alertas_criticas"
        private const val ID_BASE = 1000
    }
}
