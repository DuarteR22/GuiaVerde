package com.guiaverde.app.data.remote

import com.guiaverde.app.domain.model.Coordenadas
import com.guiaverde.app.domain.repository.RotaRepository
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val URL_BASE_OSRM = "https://router.project-osrm.org/"

/**
 * Implementação de [RotaRepository] com o servidor público de
 * demonstração do OSRM. `object`, mesmo raciocínio dos outros
 * repositórios: é só uma fonte de dados sem estado próprio que mude.
 *
 * Nota (fica escrita porque foi discutida com o utilizador): este
 * servidor não pede chave, mas tem limite de uso — não mais de 1
 * pedido/segundo, uso não-comercial. Bem dentro do que esta app faz (um
 * pedido por cada "Calcular Portagens"); só relevante se um dia isto
 * tiver tráfego a sério, altura em que faria sentido hospedar uma
 * instância própria do OSRM.
 */
object OsrmRotaRepository : RotaRepository {

    private val api: OsrmApi = Retrofit.Builder()
        .baseUrl(URL_BASE_OSRM)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OsrmApi::class.java)

    override suspend fun obterRota(origem: Coordenadas, destino: Coordenadas): List<Coordenadas> {
        // O OSRM quer "longitude,latitude" (ordem trocada) para cada
        // ponto, separados por ";" — exatamente o formato pedido.
        val segmento = "${origem.longitude},${origem.latitude};${destino.longitude},${destino.latitude}"

        return try {
            api.obterRota(coordenadas = segmento, overview = "full", geometries = "geojson")
                .routes
                ?.firstOrNull()
                ?.geometry
                ?.coordinates
                ?.mapNotNull { it.paraCoordenadas() }
                ?: emptyList()
        } catch (e: IOException) {
            // Sem rede, timeout, DNS falhou, etc. — sem rota disponível.
            emptyList()
        } catch (e: HttpException) {
            // O OSRM respondeu com um erro HTTP (ex: sem rota possível entre os dois pontos).
            emptyList()
        }
    }
}

/** Um par do GeoJSON `[longitude, latitude]` → [Coordenadas]; descarta pares incompletos. */
private fun List<Double>.paraCoordenadas(): Coordenadas? {
    if (size < 2) return null
    return Coordenadas(latitude = this[1], longitude = this[0])
}
