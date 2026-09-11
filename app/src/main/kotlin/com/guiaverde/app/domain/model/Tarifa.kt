package com.guiaverde.app.domain.model

/**
 * O preço para uma [Classe] de veículo entre uma [Portagem] de entrada e
 * uma de saída. Mapeia a tabela `Tarifa`.
 *
 * Nota de negócio: para pórticos SCUT (sistema aberto), `idEntrada` e
 * `idSaida` são o MESMO id de Portagem — não há um par entrada/saída
 * distinto, o pórtico cobra um valor fixo por passagem.
 *
 * `preco` usa Double por simplicidade nesta fase do MVP. Para dinheiro a
 * sério (nomeadamente se um dia isto processar pagamentos), o normal é
 * evitar `Double`/`Float` — os erros de arredondamento em vírgula
 * flutuante binária acumulam-se — e usar `BigDecimal` ou inteiros em
 * cêntimos. Fica anotado para revermos antes disso ser relevante.
 */
data class Tarifa(
    val idTarifa: Long,
    val preco: Double,
    val idClasse: Long,
    val idEntrada: Long,
    val idSaida: Long
)
