package com.guiaverde.app.domain.model

/**
 * O preço de UM troço de autoestrada — a versão real de
 * [com.guiaverde.app.domain.calcularCustoPortagens], resultado do Passo
 * 12 (fase 2: preços). Um troço é:
 * - sistema fechado (A1): da primeira à última [Portagem] atravessada
 *   nessa autoestrada (paga-se uma vez, por distância).
 * - sistema aberto/SCUT: cada pórtico é o seu próprio "troço" de um só
 *   ponto (paga-se um valor fixo por pórtico).
 *
 * Tem o mesmo formato de campos que [TrocoAutoestrada] de propósito — é
 * o que permite mostrar isto no [com.guiaverde.app.ui.components.resumo.CartaoTrocoAutoestrada]
 * já existente, sem precisar de um componente novo.
 *
 * [custo] é `null`, deliberadamente, quando não há [Tarifa] para este
 * par — nunca mostrar 0€ ou inventar um valor: mais vale dizer "não
 * sabemos" do que mostrar um preço errado.
 * [distanciaKm] é em linha reta (Haversine) entre origem e destino, não a
 * distância real da estrada — aproximação, não temos a distância do
 * trajeto por troço, só o total da viagem.
 */
data class SegmentoPreco(
    val origem: String,
    val destino: String,
    val custo: Double?,
    val tipo: TipoPortagem,
    val siglas: List<String>,
    val distanciaKm: Int,
    val numeroPassagens: Int
)
