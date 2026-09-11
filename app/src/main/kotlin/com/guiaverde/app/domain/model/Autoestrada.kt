package com.guiaverde.app.domain.model

/**
 * Uma autoestrada ou via com portagem (ex: A1, A2, A22). Mapeia a tabela
 * `Autoestrada`.
 */
data class Autoestrada(
    val idAutoestrada: Long,
    val nome: String
)
