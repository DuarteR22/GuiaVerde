package com.guiaverde.app.ui.components.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.TripOrigin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.guiaverde.app.domain.model.SugestaoLocal

/**
 * O cartão Origem/Destino do ecrã "Início e Cálculo". Não guarda o texto
 * dos campos — recebe [origem]/[destino] de fora e devolve mudanças pelos
 * callbacks `on*Change`. Chama-se "state hoisting": quem chama este
 * Composable (o ecrã) é o "dono" do estado, isto aqui é só apresentação.
 * Vantagem prática: dá para pré-visualizar (@Preview) e testar este card
 * sozinho, sem arrastar o ecrã todo.
 */
@Composable
fun OrigemDestinoCard(
    origem: String,
    destino: String,
    onOrigemChange: (String) -> Unit,
    onDestinoChange: (String) -> Unit,
    onTrocarClick: () -> Unit,
    onLocalizacaoAtualClick: () -> Unit,
    onLimparDestinoClick: () -> Unit,
    modifier: Modifier = Modifier,
    sugestoesOrigem: List<SugestaoLocal> = emptyList(),
    onSugestaoOrigemClick: (SugestaoLocal) -> Unit = {},
    sugestoesDestino: List<SugestaoLocal> = emptyList(),
    onSugestaoDestinoClick: (SugestaoLocal) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CampoLocalizacao(
                label = "Ponto de Partida",
                indicadorPreenchido = false,
                value = origem,
                onValueChange = onOrigemChange,
                placeholder = "Ex: Lisboa ou Porto",
                leadingIcon = Icons.Outlined.TripOrigin,
                trailingContent = {
                    IconButton(onClick = onLocalizacaoAtualClick) {
                        Icon(
                            imageVector = Icons.Filled.MyLocation,
                            contentDescription = "Usar localização atual",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                sugestoes = sugestoesOrigem,
                onSugestaoClick = onSugestaoOrigemClick
            )

            LinhaTrocar(onTrocarClick = onTrocarClick)

            CampoLocalizacao(
                label = "Destino",
                indicadorPreenchido = true,
                value = destino,
                onValueChange = onDestinoChange,
                placeholder = "Ex: Faro, Coimbra ou Braga",
                leadingIcon = Icons.Filled.LocationOn,
                trailingContent = {
                    // Só mostra o "X" de limpar quando há algo para limpar.
                    if (destino.isNotEmpty()) {
                        IconButton(onClick = onLimparDestinoClick) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Limpar campo de destino",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                sugestoes = sugestoesDestino,
                onSugestaoClick = onSugestaoDestinoClick
            )
        }
    }
}

/**
 * Um campo de texto com etiqueta (bolinha + label) por cima — usado para
 * Origem e Destino. Desde o Passo 12, mostra também a lista de
 * [sugestoes] do autocompletar por baixo do campo, quando não está vazia
 * — ver [ListaSugestoes].
 */
@Composable
private fun CampoLocalizacao(
    label: String,
    indicadorPreenchido: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    sugestoes: List<SugestaoLocal> = emptyList(),
    onSugestaoClick: (SugestaoLocal) -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            IndicadorPonto(preenchido = indicadorPreenchido)
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(imageVector = leadingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = trailingContent,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.6f)
            )
        )
        ListaSugestoes(sugestoes = sugestoes, onSugestaoClick = onSugestaoClick)
    }
}

/**
 * A lista suspensa do autocompletar — aparece logo por baixo do campo
 * assim que há [sugestoes], e desaparece sozinha quando a lista fica
 * vazia (depois de escolher uma, ou se o texto ficar com menos de 3
 * caracteres — ver [com.guiaverde.app.ui.viewmodel.CalculoViagemViewModel]).
 * É uma lista normal dentro do fluxo do ecrã (empurra o que vem a seguir
 * para baixo), não um popup flutuante — mais simples e sem os problemas
 * de medir a largura do campo para lhe alinhar um `DropdownMenu` por cima.
 */
@Composable
private fun ListaSugestoes(
    sugestoes: List<SugestaoLocal>,
    onSugestaoClick: (SugestaoLocal) -> Unit,
    modifier: Modifier = Modifier
) {
    if (sugestoes.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        sugestoes.forEachIndexed { indice, sugestao ->
            if (indice > 0) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSugestaoClick(sugestao) }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = sugestao.nome,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
        }
    }
}

/** Bolinha antes do label: contorno para Origem, preenchida para Destino. */
@Composable
private fun IndicadorPonto(preenchido: Boolean, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .size(8.dp)
            .then(
                if (preenchido) {
                    Modifier.background(primary, CircleShape)
                } else {
                    Modifier.border(2.dp, primary, CircleShape)
                }
            )
    )
}

/**
 * A linha entre os dois campos, com o botão de trocar Origem/Destino no
 * meio. No design é uma linha tracejada; aqui simplifiquei para uma linha
 * sólida (`HorizontalDivider`) — desenhar tracejado exige um `Canvas` com
 * `PathEffect.dashPathEffect`, não vale a pena agora.
 */
@Composable
private fun LinhaTrocar(onTrocarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        IconButton(
            onClick = onTrocarClick,
            modifier = Modifier
                .size(32.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.SwapVert,
                contentDescription = "Inverter origem e destino",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
