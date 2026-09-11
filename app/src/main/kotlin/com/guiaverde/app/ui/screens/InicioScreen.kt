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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guiaverde.app.data.mock.MockPortagensRepository
import com.guiaverde.app.data.mock.MockRotasFrequentes
import com.guiaverde.app.data.mock.RotaFrequente
import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.SugestaoLocal
import com.guiaverde.app.ui.components.comuns.BotaoAcaoPrimaria
import com.guiaverde.app.ui.components.comuns.TopoGuiaVerde
import com.guiaverde.app.ui.components.inicio.ClasseVeiculoSelector
import com.guiaverde.app.ui.components.inicio.OrigemDestinoCard
import com.guiaverde.app.ui.components.inicio.PreferenciasCard
import com.guiaverde.app.ui.components.inicio.RotasFrequentesSection
import com.guiaverde.app.ui.theme.GuiaVerdeTheme

/**
 * Ecrã "Início e Cálculo" — o formulário principal (Passo 5).
 *
 * Desde o Passo 12, TODO o estado deste ecrã (`origem`, `destino`,
 * `classeSelecionadaId`, `viaVerdeAtivo`, `evitarPortagens`) vive fora
 * daqui — no [com.guiaverde.app.ui.viewmodel.CalculoViagemViewModel],
 * partilhado com os ecrãs seguintes do fluxo. Este Composable não tem
 * nenhum `remember { mutableStateOf(...) }` para dados de negócio: só
 * recebe valores prontos a mostrar + callbacks `on*` para cada interação
 * possível — é "burro" de propósito (state hoisting levado ao limite).
 * Isso inclui pequenas operações como trocar origem↔destino ou limpar o
 * destino: já não são montadas aqui (antes eram duas chamadas
 * `onOrigemChange`/`onDestinoChange` seguidas), porque "juntar dois
 * campos de estado numa só operação" é lógica de estado, não de
 * apresentação — por isso mudou-se para o ViewModel
 * ([com.guiaverde.app.ui.viewmodel.CalculoViagemViewModel.onTrocarOrigemDestino],
 * [com.guiaverde.app.ui.viewmodel.CalculoViagemViewModel.onRotaFrequenteSelecionada]).
 *
 * `classes` continua com um valor por omissão que lê o repositório
 * diretamente (tal como antes) só para o `@Preview` funcionar sozinho,
 * sem precisar de um ViewModel — em produção, quem chama este ecrã
 * ([com.guiaverde.app.ui.navigation.GuiaVerdeNavHost]) passa sempre o
 * valor vindo do `uiState`.
 */
@Composable
fun InicioScreen(
    origem: String = "Lisboa",
    onOrigemChange: (String) -> Unit = {},
    destino: String = "",
    onDestinoChange: (String) -> Unit = {},
    onTrocarClick: () -> Unit = {},
    onLimparDestinoClick: () -> Unit = {},
    sugestoesOrigem: List<SugestaoLocal> = emptyList(),
    onSugestaoOrigemClick: (SugestaoLocal) -> Unit = {},
    sugestoesDestino: List<SugestaoLocal> = emptyList(),
    onSugestaoDestinoClick: (SugestaoLocal) -> Unit = {},
    classes: List<Classe> = MockPortagensRepository.listarClasses(),
    classeSelecionadaId: Long = 1L,
    onClasseSelecionada: (Long) -> Unit = {},
    viaVerdeAtivo: Boolean = true,
    onViaVerdeChange: (Boolean) -> Unit = {},
    evitarPortagens: Boolean = false,
    onEvitarPortagensChange: (Boolean) -> Unit = {},
    onRotaFrequenteClick: (RotaFrequente) -> Unit = {},
    onCalcularClick: () -> Unit = {}
) {
    Scaffold(topBar = { TopoGuiaVerde() }) { espacamentoInterno ->
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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Planeie a sua viagem",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Calcule os custos exatos de portagens e ex-SCUTs em Portugal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OrigemDestinoCard(
                    origem = origem,
                    destino = destino,
                    onOrigemChange = onOrigemChange,
                    onDestinoChange = onDestinoChange,
                    onTrocarClick = onTrocarClick,
                    onLocalizacaoAtualClick = {
                        // Passo futuro: pedir permissão de localização e
                        // preencher com o endereço atual (reverse geocoding).
                        // Por agora, sem efeito.
                    },
                    onLimparDestinoClick = onLimparDestinoClick,
                    sugestoesOrigem = sugestoesOrigem,
                    onSugestaoOrigemClick = onSugestaoOrigemClick,
                    sugestoesDestino = sugestoesDestino,
                    onSugestaoDestinoClick = onSugestaoDestinoClick
                )

                ClasseVeiculoSelector(
                    classes = classes,
                    classeSelecionadaId = classeSelecionadaId,
                    onClasseSelecionada = onClasseSelecionada,
                    onTabelaOficialClick = {
                        // Passo futuro: abrir a tabela oficial de classes
                        // (ex: link externo ou um BottomSheet). Sem efeito por agora.
                    }
                )

                PreferenciasCard(
                    viaVerdeAtivo = viaVerdeAtivo,
                    onViaVerdeChange = onViaVerdeChange,
                    evitarPortagens = evitarPortagens,
                    onEvitarPortagensChange = onEvitarPortagensChange
                )

                BotaoAcaoPrimaria(
                    texto = "Calcular Portagens",
                    onClick = onCalcularClick
                )

                RotasFrequentesSection(
                    rotas = MockRotasFrequentes.rotas,
                    onRotaClick = onRotaFrequenteClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioScreenPreview() {
    GuiaVerdeTheme {
        InicioScreen()
    }
}
