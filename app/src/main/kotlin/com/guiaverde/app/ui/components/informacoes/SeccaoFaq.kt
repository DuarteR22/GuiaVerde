package com.guiaverde.app.ui.components.informacoes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class ItemFaq(val pergunta: String, val resposta: String)

private val perguntasFrequentes = listOf(
    ItemFaq(
        pergunta = "O que acontece se passar numa ex-SCUT sem pagar?",
        resposta = "Se ultrapassar o prazo de 15 dias úteis sem efetuar o pagamento nos CTT ou Payshop, a concessionária envia uma notificação de cobrança acrescida de custos administrativos. Caso persista o incumprimento, o processo é remetido à Autoridade Tributária com coimas significativas."
    ),
    ItemFaq(
        pergunta = "Aluguei um carro em Portugal: como pago as portagens?",
        resposta = "A maioria das empresas de Rent-a-Car oferece a ativação do identificador eletrónico no contrato por uma taxa diária reduzida. Os valores das passagens são posteriormente debitados diretamente no cartão de crédito fornecido no momento do aluguer."
    ),
    ItemFaq(
        pergunta = "Passei por engano na via Via Verde sem identificador?",
        resposta = "Nas portagens manuais tradicionais da Brisa, se entrar na faixa Via Verde por engano, não faça marcha-atrás. Deve aceder ao portal \"Pagamento de Portagens\" da concessionária 48h após a passagem e introduzir a matrícula e o trajeto percorrido para liquidar o montante em segurança."
    )
)

@Composable
fun SeccaoFaq(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Perguntas Frequentes",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            perguntasFrequentes.forEach { item -> CartaoFaq(item) }
        }
    }
}

@Composable
private fun CartaoFaq(item: ItemFaq) {
    // Um `remember` por cartão: cada pergunta abre/fecha independentemente
    // das outras (não é um "accordion" que só deixa uma aberta de cada vez).
    var expandido by remember { mutableStateOf(false) }
    val rotacaoSeta by animateFloatAsState(targetValue = if (expandido) 180f else 0f, label = "rotacaoSeta")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandido = !expandido }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.pergunta,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (expandido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Recolher" else "Expandir",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .rotate(rotacaoSeta)
                )
            }
            if (expandido) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                Text(
                    text = item.resposta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}
