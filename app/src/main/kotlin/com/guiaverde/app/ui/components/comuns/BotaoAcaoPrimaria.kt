package com.guiaverde.app.ui.components.comuns

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.guiaverde.app.ui.theme.ChivoFontFamily

/**
 * O botão de ação principal do design (ex: "Calcular Portagens" aqui;
 * "Ver Detalhes e Pórticos" no ecrã de resumo tem o mesmo estilo) — por
 * isso vive em `components`, não dentro do ecrã "Início": é reutilizável.
 *
 * Duas coisas fora do M3 "por omissão":
 * - O texto usa Chivo (a fonte de "destinos e valores"), não a Plus
 *   Jakarta Sans do resto da UI — daí `.copy(fontFamily = ...)` sobre o
 *   `titleLarge` em vez de usar o estilo tal como está.
 * - `Modifier.shadow(...)` com `ambientColor`/`spotColor` tingidos de
 *   verde, para o "glow" do design (Level 3 do DESIGN.md), em vez da
 *   sombra cinza por omissão do `Button` (por isso desligamos a
 *   elevação própria do botão com `elevation = null`).
 */
@Composable
fun BotaoAcaoPrimaria(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowForward,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        elevation = null,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = MaterialTheme.colorScheme.primaryContainer,
                spotColor = MaterialTheme.colorScheme.primaryContainer
            )
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge.copy(fontFamily = ChivoFontFamily)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
    }
}
