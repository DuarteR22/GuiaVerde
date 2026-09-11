package com.guiaverde.app.ui.components.historico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** Os dois primeiros chips filtram mesmo a lista (ver `FiltroHistorico` no ecrã); os outros dois são só visuais por agora. */
enum class FiltroHistorico(val label: String, val icon: ImageVector) {
    TODAS("Todas as Viagens", Icons.Filled.FormatListBulleted),
    FAVORITAS("Favoritas", Icons.Filled.Star)
}

@Composable
fun FiltrosHistorico(
    filtroSelecionado: FiltroHistorico,
    onFiltroSelecionado: (FiltroHistorico) -> Unit,
    numeroFavoritas: Int,
    onEsteMesClick: () -> Unit,
    onFiltrosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        items(FiltroHistorico.entries, key = { it.name }) { filtro ->
            val selecionado = filtro == filtroSelecionado
            FilterChip(
                selected = selecionado,
                onClick = { onFiltroSelecionado(filtro) },
                label = {
                    val texto = if (filtro == FiltroHistorico.FAVORITAS) {
                        "${filtro.label} ($numeroFavoritas)"
                    } else {
                        filtro.label
                    }
                    Text(texto, style = MaterialTheme.typography.labelMedium)
                },
                leadingIcon = {
                    Icon(
                        imageVector = filtro.icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selecionado,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.secondary
                )
            )
        }

        item {
            FilterChip(
                selected = false,
                onClick = onEsteMesClick,
                label = { Text("Este Mês", style = MaterialTheme.typography.labelMedium) },
                leadingIcon = {
                    Icon(Icons.Filled.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = onFiltrosClick,
                label = { Text("Filtros", style = MaterialTheme.typography.labelMedium) },
                leadingIcon = {
                    Icon(Icons.Filled.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }
    }
}
