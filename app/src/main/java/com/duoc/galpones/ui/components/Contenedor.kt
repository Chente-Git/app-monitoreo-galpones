package com.duoc.galpones.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.duoc.galpones.GalponesApp
import com.duoc.galpones.di.AppContainer

/** Acceso corto al contenedor de dependencias desde un composable. */
@Composable
fun contenedorApp(): AppContainer =
    (LocalContext.current.applicationContext as GalponesApp).container
