package com.guiaverde.app.domain.model

/**
 * Um ponto físico de portagem: uma cabine de entrada/saída (sistema
 * fechado) ou um pórtico virtual (sistema aberto/SCUT). Mapeia a tabela
 * `Portagem`. `latitude`/`longitude` são o que cruzamos com o trajeto
 * devolvido pela API de rotas (ver [coordenadas] e
 * [com.guiaverde.app.domain.detetarPortagensAtravessadas]).
 */
data class Portagem(
    val idPortagem: Long,
    val nome: String,
    val latitude: Double,
    val longitude: Double,
    val tipo: TipoPortagem,
    val idAutoestrada: Long
) {
    /**
     * Atalho computado (não um campo à parte — nunca podia ficar
     * dessincronizado de [latitude]/[longitude]) para usar com
     * [Coordenadas.distanciaEmMetrosAte] sem repetir
     * `Coordenadas(latitude, longitude)` em cada sítio que precisa disto.
     */
    val coordenadas: Coordenadas
        get() = Coordenadas(latitude, longitude)
}
