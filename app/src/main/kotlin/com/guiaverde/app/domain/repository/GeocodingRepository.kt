package com.guiaverde.app.domain.repository

import com.guiaverde.app.domain.model.SugestaoLocal

/**
 * Contrato de pesquisa de locais — o que alimenta o autocompletar de
 * Origem/Destino. Única implementação hoje:
 * [com.guiaverde.app.data.remote.PhotonGeocodingRepository] (API pública
 * do Photon/OpenStreetMap), mas o ViewModel só conhece esta interface —
 * mesmo princípio de inversão de dependência do [PortagensRepository].
 */
interface GeocodingRepository {
    /** Pesquisa locais que correspondam a [texto]. Lista vazia = sem resultados (nunca lança erro para quem chama). */
    suspend fun pesquisarLocais(texto: String): List<SugestaoLocal>
}
