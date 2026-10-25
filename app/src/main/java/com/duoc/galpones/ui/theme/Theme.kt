package com.duoc.galpones.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Asigna cada color de la paleta clara a su rol en Material 3 (primary, surface, error...). */
private val EsquemaClaro = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = VerdeSobrePrimario,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = VerdeSobreContenedor,
    secondary = SecundarioClaro,
    onSecondary = SobreSecundarioClaro,
    secondaryContainer = ContenedorSecundarioClaro,
    onSecondaryContainer = SobreContenedorSecundarioClaro,
    tertiary = TerciarioClaro,
    onTertiary = SobreTerciarioClaro,
    tertiaryContainer = ContenedorTerciarioClaro,
    onTertiaryContainer = SobreContenedorTerciarioClaro,
    background = FondoClaro,
    onBackground = SobreFondoClaro,
    surface = FondoClaro,
    onSurface = SobreFondoClaro,
    surfaceVariant = VarianteSuperficieClaro,
    onSurfaceVariant = SobreVarianteSuperficieClaro,
    outline = BordeClaro,
    error = ErrorClaro,
    onError = SobreErrorClaro
)

/** Asigna cada color de la paleta oscura a su rol en Material 3. */
private val EsquemaOscuro = darkColorScheme(
    primary = VerdePrimarioOscuro,
    onPrimary = SobreVerdePrimarioOscuro,
    primaryContainer = VerdeContenedorOscuro,
    onPrimaryContainer = SobreVerdeContenedorOscuro,
    secondary = SecundarioOscuro,
    onSecondary = SobreSecundarioOscuro,
    secondaryContainer = ContenedorSecundarioOscuro,
    onSecondaryContainer = SobreContenedorSecundarioOscuro,
    tertiary = TerciarioOscuro,
    onTertiary = SobreTerciarioOscuro,
    tertiaryContainer = ContenedorTerciarioOscuro,
    onTertiaryContainer = SobreContenedorTerciarioOscuro,
    background = FondoOscuro,
    onBackground = SobreFondoOscuro,
    surface = FondoOscuro,
    onSurface = SobreFondoOscuro,
    surfaceVariant = VarianteSuperficieOscuro,
    onSurfaceVariant = SobreVarianteSuperficieOscuro,
    outline = BordeOscuro,
    error = ErrorOscuro,
    onError = SobreErrorOscuro
)



/** Tema de la app: elige el esquema claro u oscuro según el modo del sistema y envuelve la UI. */
@Composable
fun MonitoreoGalponesTheme(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}