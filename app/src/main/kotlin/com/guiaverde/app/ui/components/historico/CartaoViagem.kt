package com.guiaverde.app.ui.components.historico

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guiaverde.app.domain.model.ViagemHistorico
import com.guiaverde.app.ui.format.paraDuracao
import com.guiaverde.app.ui.format.paraEuros

/** Um cartão de viagem na lista "Viagens Registadas". */
@Composable
fun CartaoViagem(
    viagem: ViagemHistorico,
    onFavoritaClick: () -> Unit,
    onVerDetalhesClick: () -> Unit,
    onRecalcularClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Badges (classe, via, Via Verde/Barreiras Físicas) + favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BadgePill(
                        texto = viagem.classe.classe,
                        fundo = MaterialTheme.colorScheme.secondaryContainer,
                        corTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                        negrito = true
                    )
                    BadgePill(
                        texto = viagem.descricaoVia,
                        fundo = MaterialTheme.colorScheme.surfaceContainer,
                        corTexto = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (viagem.viaVerde) {
                        BadgePill(
                            texto = "Via Verde",
                            fundo = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            corTexto = MaterialTheme.colorScheme.primary,
                            negrito = true
                        )
                    } else {
                        BadgePill(
                            texto = "Barreiras Físicas",
                            fundo = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            corTexto = MaterialTheme.colorScheme.onSurfaceVariant,
                            negrito = true
                        )
                    }
                }
                IconButton(onClick = onFavoritaClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (viagem.favorita) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = if (viagem.favorita) "Remover dos favoritos" else "Adicionar aos favoritos",
                        tint = if (viagem.favorita) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Origem/destino (bolinhas + linha) e preço/distância
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Text(viagem.origem, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(width = 2.dp, height = 16.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Text(viagem.destino, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = viagem.custo.paraEuros(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                    Text(
                        text = "${viagem.distanciaKm} km • ~${viagem.duracaoMinutos.paraDuracao()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)

            // Data + ações rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Realizada a ${viagem.dataFormatada}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onVerDetalhesClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(viagem.labelDetalhe, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Button(
                        onClick = onRecalcularClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text(
                            text = "Recalcular",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgePill(
    texto: String,
    fundo: Color,
    corTexto: Color,
    negrito: Boolean = false
) {
    Box(
        modifier = Modifier
            .background(fundo, RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (negrito) FontWeight.Bold else FontWeight.Medium
            ),
            color = corTexto,
            maxLines = 1
        )
    }
}
