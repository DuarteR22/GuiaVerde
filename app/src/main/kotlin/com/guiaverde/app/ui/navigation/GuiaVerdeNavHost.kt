package com.guiaverde.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guiaverde.app.ui.screens.ACalcularRotaScreen
import com.guiaverde.app.ui.screens.InicioScreen
import com.guiaverde.app.ui.screens.ResumoViagemScreen
import com.guiaverde.app.ui.viewmodel.CalculoViagemViewModel

/**
 * O fluxo de cálculo: Início → A calcular rota → Resumo da Viagem.
 * `NavHost` é uma espécie de `when` vivo: mostra sempre o Composable
 * registado para a rota atual do [navController], e troca com animação
 * quando alguém chama `navController.navigate(...)`.
 *
 * Até ao Passo 10, a viagem (`origem`, `destino`, `classeSelecionadaId`)
 * vivia aqui em `remember` — era o antepassado comum de "Início" (que a
 * escrevia) e "A calcular rota"/"Resumo" (que só a liam). Passo 12: essa
 * responsabilidade passou para o [CalculoViagemViewModel], obtido com
 * `viewModel()`. O NavHost continua a ser o único sítio que conhece as 3
 * rotas — só que agora, em vez de guardar o estado, lê o `uiState` do
 * ViewModel e liga os eventos de cada ecrã aos métodos dele.
 *
 * `viewModel()` procura a instância no `ViewModelStoreOwner` mais
 * próximo — aqui, a Activity única da app — por isso esta instância
 * sobrevive a rodar o ecrã E a trocar de separador na barra de navegação
 * inferior e voltar (antes, com `remember`, a viagem perdia-se nesse
 * segundo caso, porque todo este Composable saía de composição).
 */
@Composable
fun GuiaVerdeNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: CalculoViagemViewModel = viewModel()
) {
    // collectAsStateWithLifecycle: lê o StateFlow do ViewModel e devolve um
    // State do Compose — cada emissão nova despoleta recomposição, tal como
    // um `remember { mutableStateOf(...) }`, mas a subscrição ao Flow pausa
    // sozinha quando este ecrã não está visível.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Ecra.Inicio.rota) {
        composable(Ecra.Inicio.rota) {
            InicioScreen(
                origem = uiState.origem,
                onOrigemChange = viewModel::onOrigemChange,
                destino = uiState.destino,
                onDestinoChange = viewModel::onDestinoChange,
                onTrocarClick = viewModel::onTrocarOrigemDestino,
                onLimparDestinoClick = viewModel::onLimparDestino,
                sugestoesOrigem = uiState.sugestoesOrigem,
                onSugestaoOrigemClick = viewModel::onSugestaoOrigemSelecionada,
                sugestoesDestino = uiState.sugestoesDestino,
                onSugestaoDestinoClick = viewModel::onSugestaoDestinoSelecionada,
                classes = uiState.classes,
                classeSelecionadaId = uiState.classeSelecionadaId,
                onClasseSelecionada = viewModel::onClasseSelecionada,
                viaVerdeAtivo = uiState.viaVerdeAtivo,
                onViaVerdeChange = viewModel::onViaVerdeChange,
                evitarPortagens = uiState.evitarPortagens,
                onEvitarPortagensChange = viewModel::onEvitarPortagensChange,
                onRotaFrequenteClick = viewModel::onRotaFrequenteSelecionada,
                origemDestinoValidos = uiState.origemCoordenadas != null && uiState.destinoCoordenadas != null,
                onCalcularClick = { navController.navigate(Ecra.ACalcularRota.rota) }
            )
        }
        composable(Ecra.ACalcularRota.rota) {
            // O trabalho REAL (pedido ao OSRM + deteção de portagens) vive
            // aqui, não dentro de ACalcularRotaScreen — é este NavHost que
            // já é dono do ViewModel. `LaunchedEffect(Unit)`: corre uma
            // única vez ao entrar nesta rota; se o utilizador premir
            // "Cancelar" (onCancelarClick faz popBackStack), este
            // Composable sai de composição e a coroutine cancela-se
            // sozinha — não fica a calcular em segundo plano.
            LaunchedEffect(Unit) {
                viewModel.calcularTrajeto()
                navController.navigate(Ecra.ResumoViagem.rota) {
                    // Remove "A calcular rota" do histórico: ao carregar
                    // "Voltar" a partir do Resumo, salta direto para o
                    // Início — não faria sentido voltar a um ecrã de
                    // loading que já terminou.
                    popUpTo(Ecra.Inicio.rota)
                }
            }

            ACalcularRotaScreen(
                origem = uiState.origem,
                destino = uiState.destino,
                // `classeSelecionada` já vem calculada do uiState (ver
                // CalculoViagemUiState); o fallback só existe para o caso
                // (impossível na prática, já que o mock nunca vem vazio) de
                // a lista de classes ainda não ter chegado.
                classe = uiState.classeSelecionada ?: uiState.classes.first(),
                onCancelarClick = { navController.popBackStack() }
            )
        }
        composable(Ecra.ResumoViagem.rota) {
            ResumoViagemScreen(
                resumo = viewModel.gerarResumoViagem(),
                temCoordenadas = uiState.origemCoordenadas != null && uiState.destinoCoordenadas != null,
                segmentosPreco = uiState.segmentosPreco,
                onVoltarClick = {
                    // Só volta — mantém o que estava preenchido no Início,
                    // para o utilizador poder afinar a pesquisa.
                    navController.popBackStack(Ecra.Inicio.rota, inclusive = false)
                },
                onNovaPesquisaClick = {
                    // Diferente de "voltar": limpa a viagem, para começar
                    // mesmo do zero. A limpeza em si vive no ViewModel
                    // (onNovaPesquisa) — aqui só se decide QUANDO chamá-la.
                    viewModel.onNovaPesquisa()
                    navController.popBackStack(Ecra.Inicio.rota, inclusive = false)
                }
            )
        }
    }
}
