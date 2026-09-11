package com.guiaverde.app.domain.model

/**
 * Um troço da viagem numa autoestrada (ou grupo de SCUTs contíguas, daí
 * `siglas` ser uma lista — ex: `["A29", "A44"]`). É informação já
 * CALCULADA, não uma entidade da base de dados: no MVP vem de dados mock
 * ([com.guiaverde.app.data.mock.MockResumoViagem]); no Passo 9 passa a
 * resultar de cruzar o trajeto real com [Portagem]/[Tarifa].
 */
data class TrocoAutoestrada(
    val siglas: List<String>,
    val nome: String,
    val concessionaria: String,
    val tipo: TipoPortagem,
    val distanciaKm: Int,
    val custo: Double,
    val numeroPassagens: Int
)
