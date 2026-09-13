package com.guiaverde.app.domain

import com.guiaverde.app.domain.model.Autoestrada
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.SegmentoPreco
import com.guiaverde.app.domain.model.Tarifa
import com.guiaverde.app.domain.model.TipoPortagem
import kotlin.math.roundToInt

/**
 * Calcula o preço (e o resto do que o [com.guiaverde.app.ui.components.resumo.CartaoTrocoAutoestrada]
 * precisa) de cada troço de autoestrada, a partir das portagens REAIS
 * atravessadas ([detetarPortagensAtravessadas]) — Passo 12, fase 2.
 *
 * Agrupa as portagens por autoestrada CONSECUTIVA (não por
 * `idAutoestrada` sozinho) — se a rota saísse do A1 e voltasse a entrar
 * mais tarde, isso são dois troços separados, não um só; agrupar por
 * `idAutoestrada` sem olhar à ordem juntava os dois num troço só,
 * incorretamente.
 *
 * É uma função de topo, não uma `class`/`object` — mesmo raciocínio de
 * [detetarPortagensAtravessadas]: pura, sem estado próprio.
 */
fun calcularCustoPortagens(
    portagensAtravessadas: List<Portagem>,
    classeId: Long,
    tarifas: List<Tarifa>,
    autoestradas: List<Autoestrada>
): List<SegmentoPreco> {
    return portagensAtravessadas
        .agruparPorAutoestradaConsecutiva()
        .map { grupo -> construirSegmento(grupo, classeId, tarifas, autoestradas) }
}

/** Soma total dos troços — `null` se QUALQUER UM não tiver tarifa (mostrar um total parcial seria enganoso, parece um preço final quando não é). */
fun List<SegmentoPreco>.custoTotal(): Double? {
    if (isEmpty()) return null
    if (any { it.custo == null }) return null
    return sumOf { it.custo!! }
}

private fun construirSegmento(
    grupo: List<Portagem>,
    classeId: Long,
    tarifas: List<Tarifa>,
    autoestradas: List<Autoestrada>
): SegmentoPreco {
    val entrada = grupo.first()
    val saida = grupo.last()
    val tipo = entrada.tipo

    val custo = when (tipo) {
        TipoPortagem.CABINE -> precoSistemaFechado(entrada, saida, classeId, tarifas)
        TipoPortagem.PORTICO -> precoSistemaAberto(grupo, classeId, tarifas)
    }

    // "A1 — Autoestrada do Norte" → "A1" (o que mostra no badge).
    val sigla = autoestradas
        .firstOrNull { it.idAutoestrada == entrada.idAutoestrada }
        ?.nome
        ?.substringBefore('—')
        ?.trim()

    return SegmentoPreco(
        origem = entrada.nome,
        destino = saida.nome,
        custo = custo,
        tipo = tipo,
        siglas = listOfNotNull(sigla),
        // Linha reta entre origem e destino — aproximação (ver KDoc de SegmentoPreco).
        distanciaKm = (entrada.coordenadas.distanciaEmMetrosAte(saida.coordenadas) / 1000).roundToInt(),
        numeroPassagens = grupo.size
    )
}

/**
 * Sistema fechado (A1): paga-se UMA vez, da entrada à saída do grupo —
 * nunca uma vez por cada portagem intermédia. A tabela de [tarifas] só
 * tem cada par escrito num sentido (ex: Alverca→Coimbra), mas o preço de
 * um troço fechado é o mesmo nos dois sentidos — por isso a pesquisa
 * aceita `idEntrada`/`idSaida` trocados (Coimbra→Alverca tem de encontrar
 * a mesma tarifa que Alverca→Coimbra).
 */
private fun precoSistemaFechado(entrada: Portagem, saida: Portagem, classeId: Long, tarifas: List<Tarifa>): Double? {
    return tarifas.firstOrNull {
        it.idClasse == classeId &&
            ((it.idEntrada == entrada.idPortagem && it.idSaida == saida.idPortagem) ||
                (it.idEntrada == saida.idPortagem && it.idSaida == entrada.idPortagem))
    }?.preco
}

/** Sistema aberto/SCUT: cada pórtico paga um valor fixo, próprio (idEntrada == idSaida); soma-se todos os do grupo. */
private fun precoSistemaAberto(grupo: List<Portagem>, classeId: Long, tarifas: List<Tarifa>): Double? {
    var total = 0.0
    for (portico in grupo) {
        val precoPortico = tarifas.firstOrNull {
            it.idClasse == classeId && it.idEntrada == portico.idPortagem && it.idSaida == portico.idPortagem
        }?.preco ?: return null
        total += precoPortico
    }
    return total
}

/** Junta portagens seguidas da MESMA autoestrada num grupo — mantém a ordem do trajeto, nunca mistura grupos não-consecutivos. */
private fun List<Portagem>.agruparPorAutoestradaConsecutiva(): List<List<Portagem>> {
    val grupos = mutableListOf<MutableList<Portagem>>()
    for (portagem in this) {
        val ultimoGrupo = grupos.lastOrNull()
        if (ultimoGrupo != null && ultimoGrupo.last().idAutoestrada == portagem.idAutoestrada) {
            ultimoGrupo.add(portagem)
        } else {
            grupos.add(mutableListOf(portagem))
        }
    }
    return grupos
}
