package com.guiaverde.app.domain.model

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** Raio médio da Terra, em metros — usado na fórmula de Haversine abaixo. */
private const val RAIO_TERRA_METROS = 6_371_000.0

/**
 * Um par de coordenadas geográficas (latitude/longitude, WGS84 — o
 * sistema que o GPS e o OpenStreetMap usam). Existe como tipo próprio,
 * em vez de dois `Double` soltos, para nunca se trocar a ordem entre
 * latitude e longitude ao passar valores entre o geocoding, o trajeto
 * (OSRM) e a deteção de portagens.
 */
data class Coordenadas(
    val latitude: Double,
    val longitude: Double
) {
    /**
     * Distância aproximada a [outra], em metros, ao longo da superfície
     * da Terra — fórmula de Haversine (assume a Terra uma esfera
     * perfeita; o erro típico é inferior a 0,5%, mais do que suficiente
     * para decidir se um ponto da rota "passa perto" de uma portagem —
     * ver [com.guiaverde.app.domain.detetarPortagensAtravessadas]).
     */
    fun distanciaEmMetrosAte(outra: Coordenadas): Double {
        val latitude1Rad = Math.toRadians(latitude)
        val latitude2Rad = Math.toRadians(outra.latitude)
        val deltaLatitudeRad = Math.toRadians(outra.latitude - latitude)
        val deltaLongitudeRad = Math.toRadians(outra.longitude - longitude)

        val a = sin(deltaLatitudeRad / 2) * sin(deltaLatitudeRad / 2) +
            cos(latitude1Rad) * cos(latitude2Rad) *
            sin(deltaLongitudeRad / 2) * sin(deltaLongitudeRad / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return RAIO_TERRA_METROS * c
    }
}
