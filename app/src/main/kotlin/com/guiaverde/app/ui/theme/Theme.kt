package com.guiaverde.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Mapeia 1:1 as cores do DESIGN.md para os "roles" (papéis) do Material 3.
 * Um role não é "a cor X" — é "a cor para ação primária", "a cor de texto
 * sobre essa ação", etc. É por isto que os Composables mais à frente vão
 * pedir `MaterialTheme.colorScheme.primary`, nunca uma cor "PrimaryLight"
 * diretamente — trocar o tema (ex: dark mode, no futuro) não obriga a
 * tocar em nenhum ecrã.
 */
private val GuiaVerdeColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    inversePrimary = InversePrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = SurfaceTintLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    surfaceDim = SurfaceDimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight
)

// Ponto de partida: a escala tipográfica default do Material 3 — só
// substituímos (via .copy()) os "slots" para os quais o design tem valores
// explícitos. Os que sobram (displayMedium, titleSmall, bodySmall, ...)
// ficam com o default do M3, e ajustamos se/quando precisarmos deles.
private val baseTypography = Typography()

val GuiaVerdeTypography = baseTypography.copy(
    displayLarge = baseTypography.displayLarge.copy(
        fontFamily = ChivoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.02).em
    ),
    headlineLarge = baseTypography.headlineLarge.copy(
        // O design distingue headline-lg (32px, ecrãs largos) de
        // headline-lg-mobile (26px). Esta app é só telemóvel, por isso
        // usamos diretamente o valor mobile aqui, sem variante "desktop".
        fontFamily = ChivoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.01).em
    ),
    headlineMedium = baseTypography.headlineMedium.copy(
        fontFamily = ChivoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleLarge = baseTypography.titleLarge.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = baseTypography.titleMedium.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = baseTypography.bodyLarge.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = baseTypography.bodyMedium.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = baseTypography.labelLarge.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.01.em
    ),
    labelMedium = baseTypography.labelMedium.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.02.em
    ),
    labelSmall = baseTypography.labelSmall.copy(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.03.em
    )
)

/**
 * Tema único da app: o DESIGN.md só define uma variante ("light"), por
 * isso não ramificamos por isSystemInDarkTheme() por agora — cada ecrã só
 * precisa de envolver o seu conteúdo nisto para herdar cores e tipografia.
 */
@Composable
fun GuiaVerdeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GuiaVerdeColorScheme,
        typography = GuiaVerdeTypography,
        content = content
    )
}
