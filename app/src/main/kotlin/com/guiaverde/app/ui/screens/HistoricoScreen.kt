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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guiaverde.app.data.mock.MockHistoricoViagens
import com.guiaverde.app.domain.model.ViagemHistorico
import com.guiaverde.app.ui.components.comuns.TopoGuiaVerde
import com.guiaverde.app.ui.components.historico.BannerRelatorioDespesas
import com.guiaverde.app.ui.components.historico.CartaoResumoMensal
import com.guiaverde.app.ui.components.historico.CartaoViagem
import com.guiaverde.app.ui.components.historico.FiltroHistorico
import com.guiaverde.app.ui.components.historico.FiltrosHistorico
import com.guiaverde.app.ui.theme.GuiaVerdeTheme

/**
 * Ecrã "Histórico de Viagens". `viagens` vive aqui — é o único ecrã que
 * precisa de o alterar (marcar/desmarcar favorito), por isso não subiu
 * para o `GuiaVerdeApp` nem para nenhum NavHost (compara com
 * `origem`/`destino` no fluxo de cálculo, que tiveram de subir por serem
 * partilhados entre ecrãs — aqui não há esse problema).
 */
@Composable
fun HistoricoScreen(modifier: Modifier = Modifier) {
    var viagens by remember { mutableStateOf(MockHistoricoViagens.viagens) }
    var filtro by remember { mutableStateOf(FiltroHistorico.TODAS) }

    val viagensFiltradas = when (filtro) {
        FiltroHistorico.TODAS -> viagens
        FiltroHistorico.FAVORITAS -> viagens.filter { it.favorita }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopoGuiaVerde(titulo = "Histórico de Viagens", onPesquisarClick = {}) }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FiltrosHistorico(
                    filtroSelecionado = filtro,
                    onFiltroSelecionado = { filtro = it },
                    numeroFavoritas = viagens.count { it.favorita },
                    onEsteMesClick = {
                        // Todas as viagens mock já são "deste mês" — sem
                        // dados reais com datas variadas, não há nada para
                        // este filtro fazer ainda.
                    },
                    onFiltrosClick = {
                        // Passo futuro: abrir um BottomSheet com filtros
                        // avançados (intervalo de datas, classe, autoestrada...).
                    }
                )

                CartaoResumoMensal(
                    mesReferencia = MockHistoricoViagens.MES_REFERENCIA,
                    custoTotal = MockHistoricoViagens.custoTotalMes(),
                    numeroTrajetos = MockHistoricoViagens.viagens.size,
                    economiaComDescontos = MockHistoricoViagens.ECONOMIA_COM_DESCONTOS,
                    totalQuilometragem = MockHistoricoViagens.totalQuilometragemMes(),
                    onExportarRelatorioClick = {}
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Viagens Registadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Ordenado por data recente",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Lista fixa (tipicamente poucas dezenas de viagens) dentro
                // de um ecrã já scrollável — Column + forEach, mesmo
                // raciocínio das outras listas do projeto (ver
                // RotasFrequentesSection.kt). Se um dia isto vier de uma
                // base de dados com centenas de registos, aí sim passa a
                // fazer sentido paginar com LazyColumn dedicado.
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    viagensFiltradas.forEach { viagem ->
                        CartaoViagem(
                            viagem = viagem,
                            onFavoritaClick = {
                                viagens = viagens.trocarFavorita(viagem.id)
                            },
                            onVerDetalhesClick = {},
                            onRecalcularClick = {}
                        )
                    }
                }

                BannerRelatorioDespesas(onEmitirPdfClick = {})
            }
        }
    }
}

/** `.copy()` na viagem certa, dentro de uma nova lista — nunca mutamos a lista nem a viagem existente. */
private fun List<ViagemHistorico>.trocarFavorita(id: Long): List<ViagemHistorico> =
    map { viagem -> if (viagem.id == id) viagem.copy(favorita = !viagem.favorita) else viagem }

@Preview(showBackground = true)
@Composable
private fun HistoricoScreenPreview() {
    GuiaVerdeTheme {
        HistoricoScreen()
    }
}
