package com.guiaverde.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoint do servidor público de demonstração do OSRM, usado para obter
 * o trajeto real entre Origem e Destino — sem chave. `internal`: detalhe
 * de `data/remote`, o resto da app só conhece
 * [com.guiaverde.app.domain.repository.RotaRepository].
 */
internal interface OsrmApi {
    // `encoded = true`: o segmento já vem pronto ("lon,lat;lon,lat") de
    // [com.guiaverde.app.data.remote.OsrmRotaRepository] — sem isto, o
    // Retrofit tentava "escapar" a vírgula e o ponto e vírgula, partindo
    // o formato que o OSRM espera.
    @GET("route/v1/driving/{coordenadas}")
    suspend fun obterRota(
        @Path("coordenadas", encoded = true) coordenadas: String,
        @Query("overview") overview: String,
        @Query("geometries") geometries: String
    ): OsrmResposta
}

// Só os campos do GeoJSON do OSRM que realmente usamos — tal como no
// Photon, o Gson ignora em silêncio tudo o resto que venha na resposta.

internal data class OsrmResposta(
    val routes: List<OsrmRota>?
)

internal data class OsrmRota(
    val geometry: OsrmGeometria?
)

/** GeoJSON: `coordinates` vem como uma lista de `[longitude, latitude]` — mesma ordem trocada do Photon. */
internal data class OsrmGeometria(
    val coordinates: List<List<Double>>?
)
