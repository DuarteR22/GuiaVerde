package com.guiaverde.app.domain.model

/**
 * Classe de veículo para efeitos de portagem (Classe 1, 2, 3, 4, Motociclo).
 * Mapeia diretamente a tabela `Classe` da base de dados.
 */
data class Classe(
    val idClasse: Long,
    val classe: String
)
