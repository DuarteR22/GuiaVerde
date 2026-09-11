package com.guiaverde.app.ui.components.informacoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Conteúdo de referência (não muda por viagem, nem vem de cálculo
 * nenhum) — por isso fica aqui junto do componente que o mostra, em vez
 * de em `data/mock`. É a mesma lógica do banner SCUT: não há nada a
 * "mockar", é a regra real do IMT escrita em texto.
 */
private data class InfoClasse(
    val badge: String,
    val icon: ImageVector,
    val corIcon: Color,
    val titulo: String,
    val descricao: String,
    val nota: String,
    val notaIcon: ImageVector?,
    val corNota: Color
)

@Composable
private fun infoClasses(): List<InfoClasse> {
    val cores = MaterialTheme.colorScheme
    return listOf(
        InfoClasse(
            badge = "Classe 1",
            icon = Icons.Filled.DirectionsCar,
            corIcon = cores.primary,
            titulo = "Ligeiros de Passageiros",
            descricao = "Altura no 1.º eixo < 1,10m e até 2 eixos.",
            nota = "Tarifa base padrão (100%)",
            notaIcon = Icons.Filled.Info,
            corNota = cores.outline
        ),
        InfoClasse(
            badge = "Classe 2",
            icon = Icons.Filled.AirportShuttle,
            corIcon = cores.secondary,
            titulo = "SUVs & Carrinhas",
            descricao = "Altura no 1.º eixo ≥ 1,10m com 2 eixos.",
            nota = "Via Verde reduz p/ Classe 1*",
            notaIcon = Icons.Filled.CheckCircle,
            corNota = cores.secondary
        ),
        InfoClasse(
            badge = "Classe 3 & 4",
            icon = Icons.Filled.LocalShipping,
            corIcon = cores.outline,
            titulo = "Pesados e Reboques",
            descricao = "3 eixos (Classe 3) ou 4+ eixos (Classe 4).",
            nota = "Transporte rodoviário comercial",
            notaIcon = null,
            corNota = cores.outline
        ),
        InfoClasse(
            badge = "Classe 5",
            icon = Icons.Filled.TwoWheeler,
            corIcon = cores.primary,
            titulo = "Motociclos",
            descricao = "Duas rodas motorizadas com ou sem sidecar.",
            nota = "Desconto até 30% c/ Via Verde",
            notaIcon = null,
            corNota = cores.primary
        )
    )
}

@Composable
fun SeccaoClassesVeiculos(onTabelaOficialClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Classes de Veículos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Tabela Oficial IMT",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable(onClick = onTabelaOficialClick)
            )
        }

        val classes = infoClasses()
        // Grelha 2x2 fixa — 4 itens sempre, não vale a pena um LazyVerticalGrid
        // genérico para um número de células que nunca muda.
        classes.chunked(2).forEach { par ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                par.forEach { info ->
                    CartaoInfoClasse(info = info, modifier = Modifier.weight(1f))
                }
                if (par.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Text(
            text = "*A tarifa reduzida para monovolumes e crossovers exige identificador eletrónico Via Verde ativo.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun CartaoInfoClasse(info: InfoClasse, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = info.badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(imageVector = info.icon, contentDescription = null, tint = info.corIcon)
            }
            Text(
                text = info.titulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = info.descricao,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (info.notaIcon != null) {
                    Icon(
                        imageVector = info.notaIcon,
                        contentDescription = null,
                        tint = info.corNota,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = info.nota,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = info.corNota
                )
            }
        }
    }
}
