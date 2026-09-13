package com.guiaverde.app.ui.screens

import androidx.compose.foundation.background
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
import com.guiaverde.app.domain.model.ResumoViagem
import com.guiaverde.app.domain.model.SegmentoPreco
import com.guiaverde.app.domain.model.TipoPortagem
import com.guiaverde.app.domain.model.TrocoAutoestrada
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
    // Distingue "sem coordenadas escolhidas" de "coordenadas válidas mas
    // nada perto" — ver KDoc da secção "Discriminação por Autoestrada".
    temCoordenadas: Boolean = true,
    // Passo 12 (fase 2, preços): preço real por troço, calculado a partir
    // das portagens detetadas + [com.guiaverde.app.domain.model.Tarifa].
    segmentosPreco: List<SegmentoPreco> = emptyList()
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
                CartaoCustoTotal(resumo = resumo, segmentosPreco = segmentosPreco)

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
                            text = "${segmentosPreco.size} troços",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Lista pequena e fixa dentro de um ecrã já scrollável —
                    // Column + forEach, não LazyColumn (ver nota em
                    // RotasFrequentesSection.kt, mesmo raciocínio aqui).
                    if (segmentosPreco.isEmpty()) {
                        TextoSemTrocos(temCoordenadas = temCoordenadas)
                    } else {
                        segmentosPreco.forEach { segmento -> CartaoOuAvisoTroco(segmento = segmento) }
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
 * Mostra um [SegmentoPreco] no componente "a sério" já desenhado para
 * isto ([CartaoTrocoAutoestrada]) — construído a partir dos dados reais
 * (deteção + preços). Se faltar tarifa (`custo == null`), não dá para
 * montar um [TrocoAutoestrada] válido (o campo `custo` de lá não é
 * opcional) — mostra-se antes uma linha de aviso simples, nunca 0€.
 */
@Composable
private fun CartaoOuAvisoTroco(segmento: SegmentoPreco, modifier: Modifier = Modifier) {
    val custoConhecido = segmento.custo
    if (custoConhecido != null) {
        CartaoTrocoAutoestrada(
            modifier = modifier,
            troco = TrocoAutoestrada(
                siglas = segmento.siglas,
                nome = "${segmento.origem} → ${segmento.destino}",
                // Ainda não existe este campo no domínio para portagens reais
                // (só o mock tinha) — aproximação razoável por sistema: A1 e
                // as outras autoestradas "normais" são Brisa; SCUT/pórticos
                // são geridas pela Infraestruturas de Portugal.
                concessionaria = if (segmento.tipo == TipoPortagem.CABINE) "Brisa" else "Infraestruturas de Portugal",
                tipo = segmento.tipo,
                distanciaKm = segmento.distanciaKm,
                custo = custoConhecido,
                numeroPassagens = segmento.numeroPassagens
            )
        )
    } else {
        Text(
            modifier = modifier,
            text = "• ${segmento.origem} → ${segmento.destino}: tarifa não disponível",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Texto mostrado em "Discriminação por Autoestrada" quando não há
 * nenhum troço a mostrar — distingue as duas razões possíveis: sem
 * isto, "sem coordenadas" e "coordenadas válidas mas nada perto"
 * pareciam o mesmo erro (foi exatamente esta confusão que aconteceu a
 * testar Lisboa → Porto sem escolher sugestões do autocompletar).
 */
@Composable
private fun TextoSemTrocos(temCoordenadas: Boolean, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = if (!temCoordenadas) {
            "Sem coordenadas de Origem/Destino — escolhe uma sugestão da lista " +
                "do autocompletar (não basta escrever o texto) antes de calcular."
        } else {
            "Nenhuma portagem detetada perto do trajeto."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = if (!temCoordenadas) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    )
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
