package com.guiaverde.app.domain.model

/**
 * Um registo de uma viagem já calculada/realizada — o que o ecrã
 * "Histórico" lista. Ao contrário de [ResumoViagem] (o resultado de UM
 * cálculo, ainda "quente"), isto é o que ficaria persistido (Room, no
 * futuro) depois de o utilizador guardar/realizar uma viagem.
 *
 * `dataFormatada` é uma simplificação deliberada: guardamos já o texto
 * ("24 Fev, 14:20") em vez de um timestamp real (`java.time.LocalDateTime`).
 * Isso exigiria ativar "core library desugaring" no Gradle para
 * suportar `java.time` até ao minSdk 24 — decidimos adiar isso para
 * quando houver dados reais a persistir (Passo 9), não vale a pena só
 * para mock.
 */
data class ViagemHistorico(
    val id: Long,
    val classe: Classe,
    val descricaoVia: String,
    val viaVerde: Boolean,
    val favorita: Boolean,
    val origem: String,
    val destino: String,
    val custo: Double,
    val distanciaKm: Int,
    val duracaoMinutos: Int,
    val dataFormatada: String,
    val labelDetalhe: String
)
