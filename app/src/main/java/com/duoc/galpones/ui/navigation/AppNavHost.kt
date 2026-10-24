package com.duoc.galpones.ui.navigation

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.galpones.ui.alertas.AlertasScreen
import com.duoc.galpones.ui.components.PantallaPlaceholder
import com.duoc.galpones.ui.login.LoginScreen
import com.duoc.galpones.ui.resumen.ResumenScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {

    val volver: () -> Unit = { navController.popBackStack() }

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                onIngresoExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(Rutas.ALERTAS) {
            AlertasScreen(modifier = Modifier.statusBarsPadding())
        }

        composable(
            route = Rutas.ACCION,
            arguments = listOf(navArgument(Rutas.ARG_ALERTA_ID) { type = NavType.LongType })
        ) { entrada ->
            val alertaId = entrada.arguments?.getLong(Rutas.ARG_ALERTA_ID) ?: 0L
            PantallaPlaceholder(
                titulo = "Registrar acción",
                descripcion = "Pantalla pendiente (Integrante B). Alerta asociada: $alertaId",
                onVolver = volver
            )
        }

        composable(Rutas.RESUMEN) {
            ResumenScreen(modifier = Modifier.statusBarsPadding())
        }

        // ---- Integrante A (reemplaza el placeholder por su pantalla) ----
        composable(Rutas.INICIO) {
            PantallaPlaceholder(
                titulo = "Galpones",
                descripcion = "Pantalla pendiente (Integrante A): lista de galpones.",
                enlaces = listOf<Pair<String, () -> Unit>>(
                    "Detalle del galpón 1" to { navController.navigate(Rutas.detalle(1)) },
                    "Historial" to { navController.navigate(Rutas.HISTORIAL) },
                    "Umbrales" to { navController.navigate(Rutas.UMBRALES) },
                    "Registrar observación" to { navController.navigate(Rutas.OBSERVACION) },
                    "Alertas" to { navController.navigate(Rutas.ALERTAS) },
                    "Resumen jefatura" to { navController.navigate(Rutas.RESUMEN) },
                    "Cerrar sesión" to {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument(Rutas.ARG_GALPON_ID) { type = NavType.IntType })
        ) { entrada ->
            val galponId = entrada.arguments?.getInt(Rutas.ARG_GALPON_ID) ?: 0
            PantallaPlaceholder(
                titulo = "Detalle del galpón",
                descripcion = "Pantalla pendiente (Integrante A). Galpón: $galponId",
                onVolver = volver
            )
        }

        composable(Rutas.HISTORIAL) {
            PantallaPlaceholder("Historial", "Pantalla pendiente (Integrante A).", onVolver = volver)
        }

        composable(Rutas.UMBRALES) {
            PantallaPlaceholder("Umbrales", "Pantalla pendiente (Integrante A).", onVolver = volver)
        }

        composable(Rutas.OBSERVACION) {
            PantallaPlaceholder("Registrar observación", "Pantalla pendiente (Integrante A).", onVolver = volver)
        }
    }
}