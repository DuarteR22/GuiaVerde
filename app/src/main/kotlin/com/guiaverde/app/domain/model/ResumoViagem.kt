package com.guiaverde.app.domain.model

/**
 * O resultado final do cálculo de uma viagem — o que o ecrã "Resumo da
 * Viagem" mostra. Tal como [TrocoAutoestrada], é um resultado CALCULADO
 * (mock por agora, real a partir do Passo 9), não uma tabela da base de
 * dados.
 */
data class ResumoViagem(
    val origemDetalhe: String,
    val destinoDetalhe: String,
    val classe: Classe,
    val custoTotal: Double,
    val distanciaTotalKm: Int,
    val duracaoMinutos: Int,
    val trocos: List<TrocoAutoestrada>,
    val consumoMedioL100km: Double,
    val custoCombustivelEstimado: Double
) {
    // Propriedades calculadas a partir de `trocos` — não guardadas à
    // parte, para nunca poderem ficar "dessincronizadas" da lista real.
    val numeroPracasFisicas: Int
        get() = trocos.filter { it.tipo == TipoPortagem.CABINE }.sumOf { it.numeroPassagens }

    val numeroPorticos: Int
        get() = trocos.filter { it.tipo == TipoPortagem.PORTICO }.sumOf { it.numeroPassagens }
}
