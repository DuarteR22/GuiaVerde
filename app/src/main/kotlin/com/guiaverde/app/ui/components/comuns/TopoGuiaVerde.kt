package com.guiaverde.app.ui.components.comuns

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guiaverde.app.ui.theme.ChivoFontFamily

/**
 * A barra de topo partilhada: menu + título (+ subtítulo opcional) +
 * (opcionalmente) pesquisa + ajuda. Usada por "Início", "A calcular
 * rota", "Histórico" e "Informações" — só o título/subtítulo mudam, e só
 * "Histórico" (pesquisa) e "Informações" (subtítulo) usam os parâmetros
 * opcionais. O ecrã "Resumo" usa uma barra DIFERENTE
 * ([com.guiaverde.app.ui.components.resumo.TopoComVoltarPartilha] — seta
 * de voltar + partilhar), por isso não está aqui dentro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopoGuiaVerde(
    modifier: Modifier = Modifier,
    titulo: String = "Guia Verde",
    subtitulo: String? = null,
    onMenuClick: () -> Unit = {},
    onPesquisarClick: (() -> Unit)? = null,
    onAjudaClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (subtitulo == null) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = ChivoFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Column {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = ChivoFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            if (onPesquisarClick != null) {
                IconButton(onClick = onPesquisarClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Pesquisar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onAjudaClick) {
                Icon(
                    imageVector = Icons.Filled.HelpOutline,
                    contentDescription = "Ajuda e suporte",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
