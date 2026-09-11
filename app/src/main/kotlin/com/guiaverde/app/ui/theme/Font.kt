// FontVariation (variable fonts) ainda é uma API experimental do Compose —
// este "opt-in" a nível de ficheiro é o Kotlin a exigir que reconheças
// isso explicitamente antes de a usares (pode mudar em versões futuras).
@file:OptIn(ExperimentalTextApi::class)

package com.guiaverde.app.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.guiaverde.app.R

/**
 * Chivo e Plus Jakarta Sans (Google Fonts) são "variable fonts": UM único
 * ficheiro .ttf contém todos os pesos (ex: 100 a 900), em vez de teres um
 * ficheiro por peso como nas fontes estáticas tradicionais (Regular.ttf,
 * Bold.ttf, ...). Escolhes o peso em runtime através de um "eixo de
 * variação" — FontVariation.weight(x) — daí cada entrada abaixo apontar
 * para o MESMO recurso, só mudando o peso pedido.
 */
private fun chivoWeight(weight: FontWeight) = Font(
    resId = R.font.chivo_variable,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

private fun plusJakartaSansWeight(weight: FontWeight) = Font(
    resId = R.font.plus_jakarta_sans_variable,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

// Pesos usados pela escala tipográfica do design (ver Theme.kt).
val ChivoFontFamily = FontFamily(
    chivoWeight(FontWeight.SemiBold), // 600 — headline-md
    chivoWeight(FontWeight.Bold),     // 700 — display-lg, headline-lg
    chivoWeight(FontWeight.ExtraBold) // 800 — reserva para ênfases fortes
)

val PlusJakartaSansFontFamily = FontFamily(
    plusJakartaSansWeight(FontWeight.Normal),   // 400 — body-lg / body-md
    plusJakartaSansWeight(FontWeight.Medium),   // 500 — label-sm
    plusJakartaSansWeight(FontWeight.SemiBold), // 600 — title-md, label-lg/md
    plusJakartaSansWeight(FontWeight.Bold)      // 700 — title-lg
)
