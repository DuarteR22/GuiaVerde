package com.guiaverde.app.data.mock

import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.ResumoViagem
import com.guiaverde.app.domain.model.TipoPortagem
import com.guiaverde.app.domain.model.TrocoAutoestrada

/**
 * Gera um [ResumoViagem] fixo (Lisboa → Porto, tal como no design), só
 * variando a [Classe] escolhida — é o suficiente para o ecrã "Resumo"
 * ter conteúdo real. No Passo 9 isto deixa de existir: o resumo passa a
 * vir do cálculo a sério sobre o trajeto devolvido pela API de rotas.
 */
object MockResumoViagem {
    fun gerar(classe: Classe): ResumoViagem = ResumoViagem(
        origemDetalhe = "Lisboa (Alverca)",
        destinoDetalhe = "Porto (Freixo)",
        classe = classe,
        custoTotal = 23.90,
        distanciaTotalKm = 314,
        duracaoMinutos = 175, // 2h 55m
        trocos = listOf(
            TrocoAutoestrada(
                siglas = listOf("A1"),
                nome = "Autoestrada do Norte",
                concessionaria = "Brisa",
                tipo = TipoPortagem.CABINE,
                distanciaKm = 298,
                custo = 22.40,
                numeroPassagens = 4
            ),
            TrocoAutoestrada(
                siglas = listOf("A29", "A44"),
                nome = "Pórticos SCUT",
                concessionaria = "Infraestruturas de Portugal",
                tipo = TipoPortagem.PORTICO,
                distanciaKm = 16,
                custo = 1.50,
                numeroPassagens = 2
            )
        ),
        consumoMedioL100km = 6.2,
        custoCombustivelEstimado = 28.50
    )
}
