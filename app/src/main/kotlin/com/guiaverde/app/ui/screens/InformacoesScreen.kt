package com.guiaverde.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guiaverde.app.ui.components.comuns.TopoGuiaVerde
import com.guiaverde.app.ui.components.informacoes.BannerIsencaoScut
import com.guiaverde.app.ui.components.informacoes.BannerVerificarValores
import com.guiaverde.app.ui.components.informacoes.BarraPesquisaInformacoes
import com.guiaverde.app.ui.components.informacoes.SeccaoClassesVeiculos
import com.guiaverde.app.ui.components.informacoes.SeccaoConcessionarias
import com.guiaverde.app.ui.components.informacoes.SeccaoFaq
import com.guiaverde.app.ui.components.informacoes.SeccaoMetodosPagamento
import com.guiaverde.app.ui.theme.GuiaVerdeTheme

/**
 * Ecrã "Informações e Tarifários" (Passo 10) — conteúdo de referência:
 * classes de veículos, formas de pagamento, concessionárias e FAQ. Ao
 * contrário dos outros ecrãs, quase nada aqui vem de `data/mock` — é
 * texto real (a lei das ex-SCUT, as regras do IMT), por isso vive direto
 * nos componentes de `ui/components/informacoes/` em vez de simular uma
 * fonte de dados que, na prática, seria sempre conteúdo escrito à mão.
 */
@Composable
fun InformacoesScreen(modifier: Modifier = Modifier) {
    var pesquisa by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopoGuiaVerde(
                titulo = "Guia Verde",
                subtitulo = "Tarifários & Regras",
                onPesquisarClick = null
            )
        }
    ) { espacamentoInterno ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacamentoInterno),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                BarraPesquisaInformacoes(
                    valor = pesquisa,
                    onValorChange = { pesquisa = it },
                    onFiltrosClick = {}
                )

                BannerIsencaoScut(onConsultarDetalhesClick = {})

                SeccaoClassesVeiculos(onTabelaOficialClick = {})

                SeccaoMetodosPagamento(
                    onValidoEmClick = {},
                    onPortalCttClick = {}
                    // Passo futuro: abrir https://www.ctt.pt no browser via
                    // Intent(ACTION_VIEW) — sem efeito por agora.
                )

                SeccaoConcessionarias(onConcessionariaClick = {})

                SeccaoFaq()

                BannerVerificarValores(onVerificarClick = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InformacoesScreenPreview() {
    GuiaVerdeTheme {
        InformacoesScreen()
    }
}
