package com.guiaverde.app.ui.components.informacoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class CorMarca { PRIMARIA, SECUNDARIA, TERCIARIA }

private data class Concessionaria(
    val sigla: String,
    val nome: String,
    val cobertura: String,
    val cor: CorMarca
)

private val concessionarias = listOf(
    Concessionaria("B", "Brisa Auto-estradas", "A1, A2, A3, A5, A6, A9, A10, A12, A13, A14", CorMarca.PRIMARIA),
    Concessionaria("A", "Ascendi", "Costa de Prata, Beiras Litoral e Alta, Grande Porto", CorMarca.SECUNDARIA),
    Concessionaria("IP", "Infraestruturas de Portugal (IP)", "Ponte 25 de Abril, Ponte Vasco da Gama, Rede Geral", CorMarca.TERCIARIA),
    Concessionaria("N", "Norte Litoral & Norscut", "A28 Norte, A24 Interior Norte", CorMarca.PRIMARIA)
)

@Composable
fun SeccaoConcessionarias(onConcessionariaClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Concessionárias Oficiais",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column {
                concessionarias.forEachIndexed { indice, concessionaria ->
                    LinhaConcessionaria(
                        concessionaria = concessionaria,
                        onClick = { onConcessionariaClick(concessionaria.nome) }
                    )
                    if (indice != concessionarias.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
private fun LinhaConcessionaria(concessionaria: Concessionaria, onClick: () -> Unit) {
    val corSigla = when (concessionaria.cor) {
        CorMarca.PRIMARIA -> MaterialTheme.colorScheme.primary
        CorMarca.SECUNDARIA -> MaterialTheme.colorScheme.secondary
        CorMarca.TERCIARIA -> MaterialTheme.colorScheme.tertiary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = concessionaria.sigla,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = corSigla
                )
            }
            Column {
                Text(
                    text = concessionaria.nome,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = concessionaria.cobertura,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline
        )
    }
}
