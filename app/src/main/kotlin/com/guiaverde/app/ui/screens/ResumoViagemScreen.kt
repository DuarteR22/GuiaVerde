package com.guiaverde.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guiaverde.app.data.mock.MockPortagensRepository
import com.guiaverde.app.data.mock.MockResumoViagem
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.ResumoViagem
import com.guiaverde.app.ui.components.comuns.BotaoAcaoPrimaria
import com.guiaverde.app.ui.components.comuns.BotaoAcaoSecundaria
import com.guiaverde.app.ui.components.resumo.CartaoCombustivelEstimado
import com.guiaverde.app.ui.components.resumo.CartaoCustoTotal
import com.guiaverde.app.ui.components.resumo.CartaoTrocoAutoestrada
import com.guiaverde.app.ui.components.resumo.TopoComVoltarPartilha
import com.guiaverde.app.ui.theme.GuiaVerdeTheme

/**
 * Ecrã "Resumo da Viagem" (Passo 8, completo). Recebe um [ResumoViagem]
 * já pronto — não sabe (nem precisa de saber) se veio de dados mock ou
 * de um cálculo real (Passo 9); é só apresentação.
 */
@Composable
fun ResumoViagemScreen(
    resumo: ResumoViagem,
    onVoltarClick: () -> Unit,
    onNovaPesquisaClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPartilharClick: () -> Unit = {},
    onVerDetalhesClick: () -> Unit = {},
    // Passo 12 (fase 1, geográfica): resultado REAL — OSRM + deteção de
    // interseção, não mock. Ver [SeccaoPortagensDetetadasProvisoria].
    portagensDetetadas: List<Portagem> = emptyList(),
    // DIAGNÓSTICO temporário — distância real mínima de cada portagem do
    // mock ao trajeto, para perceber porque é que uma detetada "devia"
    // aparecer e não aparece (ver CalculoViagemViewModel.calcularTrajeto).
    diagnosticoDistancias: List<Pair<Portagem, Double>> = emptyList()
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopoComVoltarPartilha(onVoltarClick = onVoltarClick, onPartilharClick = onPartilharClick) }
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
                SeccaoPortagensDetetadasProvisoria(
                    portagens = portagensDetetadas,
                    temCoordenadas = diagnosticoDistancias.isNotEmpty()
                )
                SeccaoDiagnosticoDistancias(diagnostico = diagnosticoDistancias)

                CartaoCustoTotal(resumo = resumo)

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discriminação por Autoestrada",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${resumo.trocos.size} troços",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Lista pequena e fixa (2-3 troços) dentro de um ecrã já
                    // scrollável — Column + forEach, não LazyColumn (ver nota
                    // em RotasFrequentesSection.kt, mesmo raciocínio aqui).
                    resumo.trocos.forEach { troco ->
                        CartaoTrocoAutoestrada(troco = troco)
                    }

                    CartaoCombustivelEstimado(
                        consumoMedioL100km = resumo.consumoMedioL100km,
                        custoEstimado = resumo.custoCombustivelEstimado
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Valores atualizados de acordo com as taxas oficiais 2024/2025.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BotaoAcaoPrimaria(
                        texto = "Ver Detalhes e Pórticos",
                        onClick = onVerDetalhesClick,
                        icon = Icons.Filled.FormatListBulleted
                    )
                    BotaoAcaoSecundaria(
                        texto = "Nova Pesquisa",
                        onClick = onNovaPesquisaClick,
                        icon = Icons.Filled.RestartAlt
                    )
                }
            }
        }
    }
}

/**
 * Secção PROVISÓRIA (Passo 12, fase 1) — mostra o resultado real da
 * deteção de portagens (OSRM + Haversine) em texto simples, só para
 * confirmar visualmente que a deteção geográfica está a funcionar. Sem
 * preços nem estilo definitivo de propósito — isto substitui-se pelos
 * cartões "a sério" (tipo [com.guiaverde.app.ui.components.resumo.CartaoTrocoAutoestrada])
 * quando a fase de preços/tarifas for implementada.
 */
@Composable
private fun SeccaoPortagensDetetadasProvisoria(
    portagens: List<Portagem>,
    // Distingue as duas razões possíveis para a lista vir vazia — sem
    // isto, "sem coordenadas" e "coordenadas válidas mas nada a 80m"
    // pareciam o mesmo erro (foi exatamente esta confusão que aconteceu
    // a testar Lisboa → Porto sem escolher sugestões do autocompletar).
    temCoordenadas: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(
                imageVector = Icons.Filled.Science,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Portagens detetadas no trajeto (provisório)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.outline
            )
        }
        if (portagens.isEmpty() && !temCoordenadas) {
            Text(
                text = "Sem coordenadas de Origem/Destino — escolhe uma sugestão da " +
                    "lista do autocompletar (não basta escrever o texto) antes de calcular.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else if (portagens.isEmpty()) {
            Text(
                text = "Nenhuma portagem detetada a menos de 80m do trajeto.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            portagens.forEach { portagem ->
                Text(
                    text = "• ${portagem.nome} (${portagem.tipo})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Secção de DIAGNÓSTICO, temporária — para tirar depois de percebermos
 * porque é que a deteção não estava a apanhar as portagens esperadas.
 * Mostra a distância real mínima de CADA portagem do mock ao trajeto,
 * ordenada da mais próxima para a mais distante (ver
 * [com.guiaverde.app.domain.distanciasMinimasPorPortagem]).
 */
@Composable
private fun SeccaoDiagnosticoDistancias(diagnostico: List<Pair<Portagem, Double>>, modifier: Modifier = Modifier) {
    if (diagnostico.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Diagnóstico: distância real ao trajeto (temporário)",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.error
        )
        diagnostico.forEach { (portagem, distanciaMetros) ->
            Text(
                text = "${portagem.nome}: ${distanciaMetros.toInt()} m",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResumoViagemScreenPreview() {
    val classe = MockPortagensRepository.listarClasses().first()
    GuiaVerdeTheme {
        ResumoViagemScreen(
            resumo = MockResumoViagem.gerar(classe),
            onVoltarClick = {},
            onNovaPesquisaClick = {}
        )
    }
}
