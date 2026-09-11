package com.guiaverde.app.ui.components.resumo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
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
import com.guiaverde.app.ui.theme.ChivoFontFamily

/**
 * A barra de topo do ecrã "Resumo" — seta de voltar + título + partilhar.
 * Diferente da [com.guiaverde.app.ui.components.comuns.TopoGuiaVerde]
 * (menu + ajuda) usada no Início e no "A calcular rota"; por isso não é
 * "comuns", vive só aqui.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopoComVoltarPartilha(
    onVoltarClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPartilharClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Resumo da Viagem",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = ChivoFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onVoltarClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            IconButton(onClick = onPartilharClick) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Partilhar trajeto",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
