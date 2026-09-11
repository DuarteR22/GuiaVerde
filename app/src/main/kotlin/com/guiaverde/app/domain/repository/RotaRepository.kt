package com.guiaverde.app.domain.repository

import com.guiaverde.app.domain.model.Coordenadas

/**
 * Contrato para obter o trajeto real entre dois pontos — só a geometria
 * da rota (a lista de coordenadas por onde passa), não o cálculo de
 * portagens em si (isso é [com.guiaverde.app.domain.detetarPortagensAtravessadas],
 * uma função separada). Única implementação hoje:
 * [com.guiaverde.app.data.remote.OsrmRotaRepository] (OSRM), mas o
 * ViewModel só conhece esta interface — mesmo princípio de inversão de
 * dependência do [PortagensRepository]/[GeocodingRepository].
 */
interface RotaRepository {
    /** Lista ORDENADA de coordenadas do trajeto de [origem] a [destino]. Lista vazia = sem rota disponível. */
    suspend fun obterRota(origem: Coordenadas, destino: Coordenadas): List<Coordenadas>
}
