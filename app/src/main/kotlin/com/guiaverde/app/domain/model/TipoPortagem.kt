package com.guiaverde.app.domain.model

/**
 * Os dois sistemas de portagem em Portugal:
 * - [CABINE]: sistema fechado — paga-se conforme a distância entre a
 *   cabine de entrada e a de saída.
 * - [PORTICO]: sistema aberto / SCUT — um pórtico virtual cobra um valor
 *   fixo ao passar. Para estes, `idEntrada == idSaida` na [Tarifa]
 *   correspondente (ver nota de negócio em Tarifa.kt).
 *
 * Um `enum class` em Kotlin é um tipo fechado: só pode valer uma destas
 * duas opções, e o compilador obriga a tratar ambas em `when` (ver uso
 * futuro no cálculo de portagens).
 */
enum class TipoPortagem {
    CABINE,
    PORTICO
}
