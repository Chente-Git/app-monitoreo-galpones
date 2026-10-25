package com.duoc.galpones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.duoc.galpones.ui.components.BannerSinConexion
import com.duoc.galpones.ui.navigation.AppNavHost
import com.duoc.galpones.ui.theme.MonitoreoGalponesTheme

/** Única Activity: pone el tema, la navegación y el aviso de sin conexión. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dibuja bajo la barra de estado; por eso las pantallas aplican statusBarsPadding.
        enableEdgeToEdge()
        val monitorRed = (application as GalponesApp).container.monitorRed
        setContent {
            MonitoreoGalponesTheme {
                // Parte en true para no mostrar el banner de sin conexión al abrir la app.
                val conectado by monitorRed.conectado.collectAsState(initial = true)
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        AppNavHost()
                    }
                    AnimatedVisibility(
                        visible = !conectado,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut()
                    ) {
                        BannerSinConexion()
                    }
                }
            }
        }
    }
}