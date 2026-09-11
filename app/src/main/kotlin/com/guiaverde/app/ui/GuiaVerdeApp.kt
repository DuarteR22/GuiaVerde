package com.guiaverde.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.guiaverde.app.ui.components.comuns.BarraNavegacaoInferior
import com.guiaverde.app.ui.components.comuns.DestinoNavegacao
import com.guiaverde.app.ui.navigation.GuiaVerdeNavHost
import com.guiaverde.app.ui.screens.HistoricoScreen
import com.guiaverde.app.ui.screens.InformacoesScreen
import com.guiaverde.app.ui.theme.GuiaVerdeTheme

/**
 * Ponto de entrada da UI: aloja a barra de navegação inferior (partilhada
 * por toda a app) e troca o conteúdo consoante o separador ativo — isto é
 * navegação de TOPO entre secções (Calcular / Histórico / Informações).
 * A navegação "a sério" entre ecrãs empilhados dentro do fluxo de
 * cálculo (Início → A calcular → Resumo) é o Passo 6, com Navigation
 * Compose — coisas diferentes, por isso não confundir.
 *
 * Com o Passo 10, os 3 separadores têm todos ecrã real — já não há
 * nenhum placeholder "por construir" nesta app.
 */
@Composable
fun GuiaVerdeApp() {
    GuiaVerdeTheme {
        var destinoSelecionado by remember { mutableStateOf(DestinoNavegacao.CALCULAR) }

        Scaffold(
            bottomBar = {
                BarraNavegacaoInferior(
                    destinoSelecionado = destinoSelecionado,
                    onDestinoSelecionado = { destinoSelecionado = it }
                )
            }
        ) { espacamentoInterno ->
            Box(modifier = Modifier.padding(espacamentoInterno)) {
                when (destinoSelecionado) {
                    DestinoNavegacao.CALCULAR -> GuiaVerdeNavHost()
                    DestinoNavegacao.HISTORICO -> HistoricoScreen()
                    DestinoNavegacao.INFORMACOES -> InformacoesScreen()
                }
            }
        }
    }
}
