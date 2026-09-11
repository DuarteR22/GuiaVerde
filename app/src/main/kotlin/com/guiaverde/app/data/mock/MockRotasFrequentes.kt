package com.guiaverde.app.data.mock

/**
 * Uma "rota frequente" NÃO é uma entidade do domínio (Passo 2) — é um
 * resumo já calculado (origem, destino, distância, custo total) pronto a
 * mostrar. Na app final isto viria de histórico do utilizador ou de rotas
 * populares agregadas no backend; por agora é uma lista fixa, só para
 * termos a secção "Rotas Frequentes" com conteúdo real.
 */
data class RotaFrequente(
    val sigla: String,
    val origem: String,
    val destino: String,
    val viaDescricao: String,
    val distanciaKm: Int,
    val custoTotal: Double,
    val classeLabel: String
)

object MockRotasFrequentes {
    val rotas: List<RotaFrequente> = listOf(
        RotaFrequente(
            sigla = "A1",
            origem = "Lisboa",
            destino = "Porto",
            viaDescricao = "Via Santarém, Leiria e Coimbra",
            distanciaKm = 312,
            custoTotal = 23.90,
            classeLabel = "Classe 1"
        ),
        RotaFrequente(
            sigla = "A2",
            origem = "Lisboa",
            destino = "Faro",
            viaDescricao = "Via Aljustrel e Albufeira",
            distanciaKm = 256,
            custoTotal = 22.15,
            classeLabel = "Classe 1"
        ),
        RotaFrequente(
            sigla = "A3",
            origem = "Porto",
            destino = "Vigo (Valença)",
            viaDescricao = "Via Braga e Ponte de Lima",
            distanciaKm = 118,
            custoTotal = 9.45,
            classeLabel = "Classe 1"
        )
    )
}
