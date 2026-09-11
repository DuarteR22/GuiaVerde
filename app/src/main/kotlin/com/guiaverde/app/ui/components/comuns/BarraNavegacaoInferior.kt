package com.guiaverde.app.ui.components.comuns

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/** Os 3 separadores de topo da app. `entries` (Kotlin 1.9+) substitui o antigo `values()`. */
enum class DestinoNavegacao(val label: String, val icon: ImageVector) {
    CALCULAR("Calcular", Icons.Filled.Route),
    HISTORICO("Histórico", Icons.Filled.History),
    INFORMACOES("Informações", Icons.Filled.Toll)
}

/**
 * Barra de navegação inferior. Uso o `NavigationBar`/`NavigationBarItem`
 * do M3 em vez de recriar à mão (Row + ícones clicáveis): já trata de
 * ripple, área de toque mínima, acessibilidade e do "indicador" (a
 * pastilha atrás do ícone selecionado) sozinho — só customizei as cores.
 *
 * Simplificação face ao design: no mockup a pastilha envolve ícone+texto;
 * o `NavigationBarItem` por omissão só a põe à volta do ícone (o texto
 * fica por baixo, sem fundo). Visualmente muito próximo, sem termos de
 * construir o componente do zero.
 */
@Composable
fun BarraNavegacaoInferior(
    destinoSelecionado: DestinoNavegacao,
    onDestinoSelecionado: (DestinoNavegacao) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        DestinoNavegacao.entries.forEach { destino ->
            NavigationBarItem(
                selected = destino == destinoSelecionado,
                onClick = { onDestinoSelecionado(destino) },
                icon = { Icon(imageVector = destino.icon, contentDescription = null) },
                label = { Text(text = destino.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
