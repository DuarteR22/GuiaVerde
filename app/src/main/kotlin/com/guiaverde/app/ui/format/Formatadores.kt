package com.guiaverde.app.ui.format

import java.util.Locale

/**
 * Formata um valor em euros à portuguesa: vírgula decimal, símbolo depois
 * do número (ex: 23,90 €). `Locale("pt", "PT")` garante a vírgula mesmo
 * que o telemóvel do utilizador esteja noutro idioma — não podemos
 * confiar no `Locale.getDefault()` do dispositivo para isto.
 */
fun Double.paraEuros(): String = String.format(Locale("pt", "PT"), "%.2f €", this)

/** Minutos totais (ex: 175) → "2h 55m" (ou só "55m" se durar menos de 1h). */
fun Int.paraDuracao(): String {
    val horas = this / 60
    val minutos = this % 60
    return if (horas > 0) "${horas}h ${minutos}m" else "${minutos}m"
}

/** Uma casa decimal à portuguesa (ex: 6.2 → "6,2") — para valores como consumo L/100km. */
fun Double.comUmaCasaDecimal(): String = String.format(Locale("pt", "PT"), "%.1f", this)
