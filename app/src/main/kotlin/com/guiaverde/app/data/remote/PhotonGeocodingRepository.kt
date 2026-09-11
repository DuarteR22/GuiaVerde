package com.guiaverde.app.data.remote

import com.guiaverde.app.domain.model.Coordenadas
import com.guiaverde.app.domain.model.SugestaoLocal
import com.guiaverde.app.domain.repository.GeocodingRepository
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val URL_BASE_PHOTON = "https://photon.komoot.io/"
private const val LIMITE_SUGESTOES = 5

// Bounding box de Portugal continental (long. mín, lat. mín, long. máx,
// lat. máx) — restringe a pesquisa do Photon ao país, tal como pedido.
private const val BBOX_PORTUGAL = "-9.5,36.9,-6.1,42.2"

/**
 * Tipos de local (`osm_value` do Photon) excluídos das sugestões —
 * NUNCA mostrados ao utilizador, só usados internamente para filtrar:
 * - Limites administrativos (distrito, concelho, país...): têm
 *   coordenadas válidas, mas o "centro" de um distrito não é um destino
 *   real de viagem — e em Portugal é comum a cidade, o concelho e o
 *   distrito partilharem o mesmo nome (ex: "Faro" é os 3), o que dava
 *   várias sugestões iguais para quem só queria escolher a cidade.
 * - Paragens de transporte público: coordenadas válidas, mas não fazem
 *   sentido como Origem/Destino de uma viagem de carro.
 */
private val TIPOS_OSM_EXCLUIDOS = setOf(
    "administrative", "state", "county", "municipality", "country",
    "stop", "bus_stop", "platform"
)

/**
 * Implementação de [GeocodingRepository] com a API pública do Photon
 * (OpenStreetMap). `object`: é só uma fonte de dados sem estado próprio
 * que mude — mesmo raciocínio do [com.guiaverde.app.data.mock.MockPortagensRepository].
 * Traduz o GeoJSON devolvido para [SugestaoLocal]; quem chama esta classe
 * nunca vê Retrofit, Gson, nem o formato GeoJSON.
 */
object PhotonGeocodingRepository : GeocodingRepository {

    private val api: PhotonApi = Retrofit.Builder()
        .baseUrl(URL_BASE_PHOTON)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PhotonApi::class.java)

    override suspend fun pesquisarLocais(texto: String): List<SugestaoLocal> {
        if (texto.isBlank()) return emptyList()

        return try {
            api.pesquisar(texto = texto, limite = LIMITE_SUGESTOES, bbox = BBOX_PORTUGAL)
                .features
                .filter { it.properties?.osmValue !in TIPOS_OSM_EXCLUIDOS }
                .mapNotNull { it.paraSugestaoLocal() }
                // Rede de segurança: se, mesmo depois do filtro acima,
                // sobrarem duas sugestões com o MESMO texto, não faz
                // sentido mostrar as duas — o utilizador não tem como as
                // distinguir, por isso só fica a primeira.
                .distinctBy { it.nome }
        } catch (e: IOException) {
            // Sem rede, timeout, DNS falhou, etc. — fica silencioso: para
            // quem escreve no campo, é como se não houvesse sugestões.
            emptyList()
        } catch (e: HttpException) {
            // O Photon respondeu com um erro HTTP (ex: 429, demasiados pedidos).
            emptyList()
        }
    }
}

/**
 * Um resultado do GeoJSON só se torna uma [SugestaoLocal] se tiver
 * coordenadas e um nome utilizável — `mapNotNull` (acima) descarta os
 * que não derem, em vez de mostrar uma sugestão vazia na lista.
 */
private fun PhotonFeature.paraSugestaoLocal(): SugestaoLocal? {
    val coordenadasGeoJson = geometry?.coordinates
    if (coordenadasGeoJson == null || coordenadasGeoJson.size < 2) return null

    val nome = nomeAmigavel() ?: return null

    return SugestaoLocal(
        nome = nome,
        // [longitude, latitude] no GeoJSON — por isso os índices vêm trocados aqui.
        coordenadas = Coordenadas(latitude = coordenadasGeoJson[1], longitude = coordenadasGeoJson[0])
    )
}

/** Junta o nome do local com a localidade (cidade/concelho/distrito), quando ajuda a desambiguar (ex: uma rua dentro de uma cidade). */
private fun PhotonFeature.nomeAmigavel(): String? {
    val principal = properties?.name ?: return null
    val localidade = properties.city ?: properties.county ?: properties.state
    return if (localidade != null && localidade != principal) "$principal, $localidade" else principal
}
