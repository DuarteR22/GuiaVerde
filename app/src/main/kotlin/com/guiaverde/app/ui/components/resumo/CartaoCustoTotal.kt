package com.guiaverde.app.ui.components.resumo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guiaverde.app.domain.custoTotal
import com.guiaverde.app.domain.model.ResumoViagem
import com.guiaverde.app.domain.model.SegmentoPreco
import com.guiaverde.app.ui.format.paraDuracao
import com.guiaverde.app.ui.format.paraEuros

/**
 * O cartão em destaque no topo do Resumo: preço total, classe, rota e as
 * 3 métricas rápidas (distância / duração / praças+pórticos).
 *
 * O preço em destaque já é REAL quando há [segmentosPreco] (Passo 12,
 * fase 2 — calculado a partir das portagens efetivamente detetadas):
 * - lista vazia (nenhum cálculo real ainda feito, ex: `@Preview`) → mostra
 *   [ResumoViagem.custoTotal] (mock), para o resto do cartão continuar a
 *   fazer sentido visualmente sem precisar de um cálculo real.
 * - lista não-vazia mas sem conseguir somar tudo (falta tarifa nalgum
 *   troço) → "N/D", nunca um valor inventado.
 * - lista completa → o total real.
 * A rota mostrada (ex: "Lisboa (Alverca) ➔ Coimbra") também já vem de
 * [segmentosPreco] quando existe — a primeira origem e o último destino
 * dos troços reais, não o par fixo do [resumo] mock. Distância, duração,
 * praças/pórticos e classe continuam a vir do [resumo] mock.
 */
@Composable
fun CartaoCustoTotal(
    resumo: ResumoViagem,
    modifier: Modifier = Modifier,
    segmentosPreco: List<SegmentoPreco> = emptyList()
) {
    val textoPreco = if (segmentosPreco.isEmpty()) {
        resumo.custoTotal.paraEuros()
    } else {
        segmentosPreco.custoTotal()?.paraEuros() ?: "N/D"
    }
    val textoRota = if (segmentosPreco.isEmpty()) {
        "${resumo.origemDetalhe} ➔ ${resumo.destinoDetalhe}"
    } else {
        "${segmentosPreco.first().origem} ➔ ${segmentosPreco.last().destino}"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column {
            // Faixa de destaque no topo do cartão — só um acento visual.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Custo Total de Portagens",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerLow,
                                RoundedCornerShape(percent = 50)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsCar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = resumo.classe.classe,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = textoPreco,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Text(
                        text = "estimado",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AltRoute,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = textoRota,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Metrica(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Straighten,
                        valor = "${resumo.distanciaTotalKm} km",
                        rotulo = "distância"
                    )
                    Metrica(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Schedule,
                        valor = resumo.duracaoMinutos.paraDuracao(),
                        rotulo = "duração"
                    )
                    Metrica(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Toll,
                        valor = "${resumo.numeroPracasFisicas} + ${resumo.numeroPorticos}",
                        rotulo = "praças / pórticos"
                    )
                }
            }
        }
    }
}

@Composable
private fun Metrica(icon: ImageVector, valor: String, rotulo: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
