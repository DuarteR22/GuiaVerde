package com.guiaverde.app.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoint da API pública do Photon (OpenStreetMap) usado para o
 * autocompletar de Origem/Destino — sem chave nem autenticação. `internal`:
 * ninguém fora de `data/remote` precisa de saber que isto existe; o resto
 * da app só conhece [com.guiaverde.app.domain.repository.GeocodingRepository].
 */
internal interface PhotonApi {
    @GET("api/")
    suspend fun pesquisar(
        @Query("q") texto: String,
        @Query("limit") limite: Int,
        @Query("bbox") bbox: String
    ): PhotonResposta
}

// As classes abaixo espelham só os campos do GeoJSON do Photon que
// realmente usamos — o Gson ignora, em silêncio, tudo o resto que venha
// na resposta. `internal` pela mesma razão de [PhotonApi]: são detalhe
// de implementação, nunca devem "escapar" para fora de `data/remote`.

internal data class PhotonResposta(
    val features: List<PhotonFeature>
)

internal data class PhotonFeature(
    val geometry: PhotonGeometria?,
    val properties: PhotonPropriedades?
)

/** GeoJSON: `coordinates` vem como `[longitude, latitude]` — ordem trocada da que o resto da app usa. */
internal data class PhotonGeometria(
    val coordinates: List<Double>?
)

internal data class PhotonPropriedades(
    val name: String?,
    val city: String?,
    val county: String?,
    val state: String?,
    // `osm_value` diz o TIPO do local ("city", "county", "state",
    // "administrative", "stop", ...) — nunca é mostrado ao utilizador, só
    // serve para FILTRAR tipos que não fazem sentido como Origem/Destino
    // de uma viagem (ver TIPOS_OSM_EXCLUIDOS em PhotonGeocodingRepository).
    @SerializedName("osm_value") val osmValue: String?
)
