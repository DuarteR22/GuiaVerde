package com.guiaverde.app.ui.components.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guiaverde.app.data.mock.RotaFrequente
import com.guiaverde.app.ui.format.paraEuros

/**
 * Secção "Rotas Frequentes": título + lista de [RotaFrequente]. É uma
 * lista pequena e fixa dentro de um ecrã que já tem scroll próprio
 * (InicioScreen usa `Modifier.verticalScroll`) — por isso um `Column`
 * normal com `forEach`, NÃO um `LazyColumn`. Duas listas verticais que
 * "scrollam" uma dentro da outra é um erro clássico em Compose (a lazy
 * pede altura infinita, a scrollável exterior também — o layout não
 * resolve e a app rebenta). `LazyColumn` só compensa com listas grandes
 * ou de tamanho variável a sério.
 */
@Composable
fun RotasFrequentesSection(
    rotas: List<RotaFrequente>,
    onRotaClick: (RotaFrequente) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Rotas Frequentes",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Estimativa Portagens",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rotas.forEach { rota ->
                RotaFrequenteItem(rota = rota, onClick = { onRotaClick(rota) })
            }
        }
    }
}

@Composable
private fun RotaFrequenteItem(rota: RotaFrequente, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                // weight(1f): sem isto, uma via longa ("Via Santarém, Leiria
                // e Coimbra...") podia espremer o preço à direita para uma
                // largura tão estreita que ele quebrava linha número a
                // número — o mesmo bug corrigido em CartaoTrocoAutoestrada.
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rota.sigla,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text(
                        text = "${rota.origem} → ${rota.destino}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${rota.viaDescricao} • ${rota.distanciaKm} km",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = rota.custoTotal.paraEuros(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = rota.classeLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
