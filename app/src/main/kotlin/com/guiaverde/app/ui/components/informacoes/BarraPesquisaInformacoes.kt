package com.guiaverde.app.ui.components.informacoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * A barra de pesquisa do topo do ecrã "Informações". Por agora é só
 * visual (o texto fica guardado, mas nada o usa para filtrar as secções
 * de baixo) — filtrar conteúdo real de várias secções ao mesmo tempo é
 * trabalho suficiente para o seu próprio passo, mais à frente.
 */
@Composable
fun BarraPesquisaInformacoes(
    valor: String,
    onValorChange: (String) -> Unit,
    onFiltrosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(end = 10.dp)
        )
        Box(modifier = Modifier.weight(1f)) {
            if (valor.isEmpty()) {
                Text(
                    text = "Pesquisar autoestrada, classe ou regras...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            BasicTextField(
                value = valor,
                onValueChange = onValorChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                )
            )
        }
        Icon(
            imageVector = Icons.Filled.Tune,
            contentDescription = "Filtros de pesquisa",
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(onClick = onFiltrosClick)
        )
    }
}
