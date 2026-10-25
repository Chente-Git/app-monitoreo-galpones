package com.duoc.galpones.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.galpones.domain.SesionActual
import com.duoc.galpones.domain.model.Rol
import com.duoc.galpones.ui.accion.AccionScreen
import com.duoc.galpones.ui.alertas.AlertasScreen
import com.duoc.galpones.ui.galpones.detalle.DetalleScreen
import com.duoc.galpones.ui.galpones.historial.HistorialScreen
import com.duoc.galpones.ui.galpones.inicio.InicioScreen
import com.duoc.galpones.ui.galpones.observacion.ObservacionScreen
import com.duoc.galpones.ui.galpones.umbrales.UmbralesScreen
import com.duoc.galpones.ui.login.LoginScreen
import com.duoc.galpones.ui.resumen.ResumenScreen

private const val DURACION_TRANSICION = 300

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {

    val volver: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(DURACION_TRANSICION)
            ) + fadeIn(animationSpec = tween(DURACION_TRANSICION))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(DURACION_TRANSICION)
            ) + fadeOut(animationSpec = tween(DURACION_TRANSICION))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(DURACION_TRANSICION)
            ) + fadeIn(animationSpec = tween(DURACION_TRANSICION))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(DURACION_TRANSICION)
            ) + fadeOut(animationSpec = tween(DURACION_TRANSICION))
        }
    ) {

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
            AlertasScreen(
                onVolver = volver,
                onAlertaClick = { id -> navController.navigate(Rutas.accion(id)) },
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(
            route = Rutas.ACCION,
            arguments = listOf(navArgument(Rutas.ARG_ALERTA_ID) { type = NavType.LongType })
        ) { entrada ->
            val alertaId = entrada.arguments?.getLong(Rutas.ARG_ALERTA_ID) ?: 0L
            AccionScreen(
                alertaId = alertaId,
                onGuardado = volver,
                onCancelar = volver,
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(Rutas.RESUMEN) {
            ResumenScreen(onVolver = volver, modifier = Modifier.statusBarsPadding())
        }

        // ---- Integrante A: módulo Monitoreo ----
        composable(Rutas.INICIO) {
            val esJefatura = SesionActual.usuario?.rol == Rol.JEFATURA
            InicioScreen(
                onGalponClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onAlertas = { navController.navigate(Rutas.ALERTAS) },
                onHistorial = { navController.navigate(Rutas.HISTORIAL) },
                onUmbrales = { navController.navigate(Rutas.UMBRALES) },
                onObservacion = { navController.navigate(Rutas.OBSERVACION) },
                onResumen = if (esJefatura) {
                    { navController.navigate(Rutas.RESUMEN) }
                } else {
                    null
                },
                onCerrarSesion = {
                    SesionActual.cerrar()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                },
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument(Rutas.ARG_GALPON_ID) { type = NavType.IntType })
        ) { entrada ->
            val galponId = entrada.arguments?.getInt(Rutas.ARG_GALPON_ID) ?: 0
            DetalleScreen(
                galponId = galponId,
                onVolver = volver,
                onHistorial = { navController.navigate(Rutas.HISTORIAL) },
                onUmbrales = { navController.navigate(Rutas.UMBRALES) },
                onObservacion = { navController.navigate(Rutas.OBSERVACION) },
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(Rutas.HISTORIAL) {
            HistorialScreen(onVolver = volver, modifier = Modifier.statusBarsPadding())
        }

        composable(Rutas.UMBRALES) {
            UmbralesScreen(
                onGuardado = volver,
                onCancelar = volver,
                modifier = Modifier.statusBarsPadding()
            )
        }

        composable(Rutas.OBSERVACION) {
            ObservacionScreen(
                onGuardado = volver,
                onCancelar = volver,
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}