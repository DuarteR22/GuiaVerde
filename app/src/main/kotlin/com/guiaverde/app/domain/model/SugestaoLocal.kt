package com.guiaverde.app.domain.model

/**
 * Um resultado de geocoding — um local real (cidade, rua, ponto de
 * interesse) devolvido pela pesquisa de autocompletar em Origem/Destino,
 * já traduzido do GeoJSON da API externa para o vocabulário do domínio.
 * Tal como [ResumoViagem], não é mock nem espelha uma tabela da base de
 * dados — é o resultado genuíno de uma fonte de dados externa (esta,
 * ao contrário do resto da app, já é real e não mock: ver
 * [com.guiaverde.app.data.remote.PhotonGeocodingRepository]).
 */
data class SugestaoLocal(
    val nome: String,
    val coordenadas: Coordenadas
)
