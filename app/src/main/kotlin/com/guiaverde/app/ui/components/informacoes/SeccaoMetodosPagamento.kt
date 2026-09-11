package com.guiaverde.app.ui.components.informacoes

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Duas cores fora da paleta verde do design system, só para estes dois
// métodos — o próprio Stitch usa âmbar/azul aqui para os distinguir
// visualmente do resto (que é tudo verde). Ficam aqui, não em ui/theme/,
// porque são uma exceção deliberada e local, não parte do tema.
private val AmbarFundo = Color(0xFFF59E0B).copy(alpha = 0.12f)
private val AmbarIcone = Color(0xFFB45309)
private val AzulFundo = Color(0xFF3B82F6).copy(alpha = 0.12f)
private val AzulIcone = Color(0xFF1D4ED8)

@Composable
fun SeccaoMetodosPagamento(
    onValidoEmClick: () -> Unit,
    onPortalCttClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Como Pagar Portagens",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }

        CartaoMetodoPagamento(
            icon = Icons.Filled.Sensors,
            corIconeFundo = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            corIcone = MaterialTheme.colorScheme.primary,
            titulo = "Via Verde (Identificador Eletrónico)",
            descricao = "Passagem direta em faixas dedicadas e cobrança automática nos pórticos eletrónicos. Não precisa de parar. Débito direto na conta bancária associada.",
            badge = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(percent = 50))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Recomendado",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
            conteudoExtra = {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable(onClick = onValidoEmClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Válido em autoestradas, parques e ferries",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        )

        CartaoMetodoPagamento(
            icon = Icons.Filled.LocalPostOffice,
            corIconeFundo = AmbarFundo,
            corIcone = AmbarIcone,
            titulo = "Pós-Pagamento CTT / Payshop",
            descricao = "Para matrículas nacionais sem dispositivo nas autoestradas com pórticos. O valor fica disponível para pagamento 48h úteis após a passagem.",
            badge = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Sem Identificador",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            },
            conteudoExtra = {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = AmbarIcone,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Prazo legal: 15 dias úteis para liquidar e evitar processo de execução fiscal.",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable(onClick = onPortalCttClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Portal de Portagens CTT",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Filled.OpenInNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        )

        CartaoMetodoPagamento(
            icon = Icons.Filled.Language,
            corIconeFundo = AzulFundo,
            corIcone = AzulIcone,
            titulo = "Matrículas Estrangeiras (EasyToll & TollCard)",
            descricao = "Turistas e viaturas de outros países devem associar o cartão de crédito à matrícula num dos postos EasyToll de fronteira (A28, A24, A25 e A22) ou comprar cartões pré-pagos virtuais.",
            conteudoExtra = {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagPagamento("EasyToll (30 dias)")
                    TagPagamento("TollCard (5€ a 40€)")
                }
            }
        )
    }
}

@Composable
private fun TagPagamento(texto: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
