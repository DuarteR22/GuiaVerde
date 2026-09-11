package com.guiaverde.app.ui.navigation

/**
 * Os 3 ecrãs do fluxo de cálculo. Chama-se [Ecra] e não "Destino" de
 * propósito — já temos `destino` (o campo do formulário) e
 * `DestinoNavegacao` (a barra inferior); um terceiro "Destino" com
 * significado diferente só ia confundir.
 *
 * `sealed class` porque o conjunto de ecrãs é fechado e conhecido em
 * tempo de compilação — o compilador avisa se um `when` (ver
 * GuiaVerdeNavHost) esquecer algum. `data object` (Kotlin 1.9+) é o jeito
 * certo de representar um singleton dentro de uma sealed hierarchy: dá
 * `toString()`/`equals()` de graça, sem repetir `object : Ecra(...)`.
 *
 * Isto é uma versão simples, baseada em Strings — o suficiente para 3
 * ecrãs sem argumentos. A Navigation Compose mais recente (2.8+) suporta
 * rotas "type-safe" com classes `@Serializable`, mas isso traz consigo o
 * plugin kotlinx.serialization; não vale a pena aqui ainda.
 */
sealed class Ecra(val rota: String) {
    data object Inicio : Ecra("inicio")
    data object ACalcularRota : Ecra("a_calcular_rota")
    data object ResumoViagem : Ecra("resumo_viagem")
}
