package com.guiaverde.app.ui.components.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RvHookup
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.guiaverde.app.domain.model.Classe

/**
 * Ícone + texto de ajuda para cada Classe — informação de APRESENTAÇÃO,
 * não faz parte do domínio ([Classe] só tem id + nome, tal como a base de
 * dados). Fica aqui, na camada de UI, e junta-se ao domínio pelo
 * `idClasse` — o mesmo princípio do repositório: cada camada só sabe o
 * que lhe compete.
 */
private data class ApresentacaoClasse(val icon: ImageVector, val descricaoAjuda: String)

private val apresentacaoPorClasse: Map<Long, ApresentacaoClasse> = mapOf(
    1L to ApresentacaoClasse(
        icon = Icons.Filled.DirectionsCar,
        descricaoAjuda = "Inclui a maioria dos automóveis ligeiros de passageiros, com altura inferior a 1,10 m ao eixo dianteiro."
    ),
    2L to ApresentacaoClasse(
        icon = Icons.Filled.AirportShuttle,
        descricaoAjuda = "Ligeiros com reboque e furgões, com altura entre 1,10 m e 2,20 m ao eixo dianteiro."
    ),
    3L to ApresentacaoClasse(
        icon = Icons.Filled.LocalShipping,
        descricaoAjuda = "Veículos pesados de 2 eixos, com altura superior a 2,20 m ao eixo dianteiro."
    ),
    4L to ApresentacaoClasse(
        icon = Icons.Filled.RvHookup,
        descricaoAjuda = "Veículos pesados com mais de 2 eixos, com altura superior a 2,20 m ao eixo dianteiro."
    ),
    5L to ApresentacaoClasse(
        icon = Icons.Filled.TwoWheeler,
        descricaoAjuda = "Motociclos pagam uma tarifa reduzida, independentemente da altura."
    )
)

/**
 * Fila horizontal com scroll de chips de seleção única (Classe do
 * Veículo), mais o texto de ajuda da classe atualmente escolhida.
 *
 * [classes] vem do repositório (Passo 4) — nomes e ids são a fonte
 * única de verdade; o mapa acima só acrescenta ícone/texto de apoio.
 */
@Composable
fun ClasseVeiculoSelector(
    classes: List<Classe>,
    classeSelecionadaId: Long,
    onClasseSelecionada: (Long) -> Unit,
    onTabelaOficialClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Classe do Veículo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = onTabelaOficialClick, contentPadding = PaddingValues(0.dp)) {
                Text(
                    text = "Tabela Oficial",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    imageVector = Icons.Filled.OpenInNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(14.dp)
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
        ) {
            items(classes, key = { it.idClasse }) { classe ->
                val apresentacao = apresentacaoPorClasse[classe.idClasse]
                val selecionada = classe.idClasse == classeSelecionadaId

                FilterChip(
                    selected = selecionada,
                    onClick = { onClasseSelecionada(classe.idClasse) },
                    label = { Text(text = classe.classe, style = MaterialTheme.typography.labelLarge) },
                    leadingIcon = apresentacao?.let { presente ->
                        {
                            Icon(
                                imageVector = presente.icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(percent = 50),
                    modifier = Modifier.height(44.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selecionada,
                        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                        selectedBorderColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        apresentacaoPorClasse[classeSelecionadaId]?.let { apresentacao ->
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = apresentacao.descricaoAjuda,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
