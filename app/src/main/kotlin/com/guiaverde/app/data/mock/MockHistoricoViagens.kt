package com.guiaverde.app.data.mock

import com.guiaverde.app.domain.model.ViagemHistorico

/**
 * As 4 viagens do design. Repara que o cartão de resumo mensal (custo
 * total, quilómetros, nº de trajetos) NÃO está aqui como valores fixos —
 * é calculado a partir desta lista (ver `custoTotalMes`, etc.), para
 * nunca poder dessincronizar dos dados reais. "Economia com descontos" é
 * a exceção: não há, no nosso modelo, nenhum conceito de "desconto
 * aplicado" por viagem, por isso fica como valor mock à parte.
 */
object MockHistoricoViagens {
    private val classe1 = MockPortagensRepository.listarClasses().first { it.idClasse == 1L }

    val viagens: List<ViagemHistorico> = listOf(
        ViagemHistorico(
            id = 1,
            classe = classe1,
            descricaoVia = "Via A1 + A29",
            viaVerde = true,
            favorita = true,
            origem = "Lisboa (Alverca)",
            destino = "Porto (Freixo)",
            custo = 23.90,
            distanciaKm = 312,
            duracaoMinutos = 175,
            dataFormatada = "24 Fev, 14:20",
            labelDetalhe = "Ver Pórticos (9)"
        ),
        ViagemHistorico(
            id = 2,
            classe = classe1,
            descricaoVia = "Via A2 (Autoestrada do Sul)",
            viaVerde = true,
            favorita = false,
            origem = "Lisboa",
            destino = "Faro (Algarve)",
            custo = 22.15,
            distanciaKm = 278,
            duracaoMinutos = 155,
            dataFormatada = "15 Fev, 09:45",
            labelDetalhe = "Ver Detalhes"
        ),
        ViagemHistorico(
            id = 3,
            classe = classe1,
            descricaoVia = "Via A3",
            viaVerde = false,
            favorita = true,
            origem = "Porto",
            destino = "Braga",
            custo = 2.15,
            distanciaKm = 56,
            duracaoMinutos = 38,
            dataFormatada = "10 Fev, 18:10",
            labelDetalhe = "Ver Portagem"
        ),
        ViagemHistorico(
            id = 4,
            classe = classe1,
            descricaoVia = "Via A1",
            viaVerde = true,
            favorita = false,
            origem = "Coimbra Sul",
            destino = "Aveiro Sul",
            custo = 4.20,
            distanciaKm = 62,
            duracaoMinutos = 42,
            dataFormatada = "28 Jan, 11:15",
            labelDetalhe = "Ver Detalhes"
        )
    )

    const val MES_REFERENCIA = "Março 2025"

    // Não é modelado noutro lado (não há "desconto" em ViagemHistorico), por
    // isso fica como mock direto — ao contrário do resto deste objeto, que é
    // tudo calculado a partir de `viagens`.
    const val ECONOMIA_COM_DESCONTOS = 12.60

    fun custoTotalMes(viagens: List<ViagemHistorico> = this.viagens): Double = viagens.sumOf { it.custo }
    fun totalQuilometragemMes(viagens: List<ViagemHistorico> = this.viagens): Int = viagens.sumOf { it.distanciaKm }
}
