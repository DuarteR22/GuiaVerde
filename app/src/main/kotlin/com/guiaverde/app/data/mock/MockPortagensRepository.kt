package com.guiaverde.app.data.mock

import com.guiaverde.app.domain.model.Autoestrada
import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.Tarifa
import com.guiaverde.app.domain.model.TipoPortagem
import com.guiaverde.app.domain.repository.PortagensRepository

/**
 * Implementação "falsa" do repositório: dados escritos à mão, só para
 * termos algo real para desenhar e testar ecrãs antes de existir uma base
 * de dados/API a sério. Cobre os dois sistemas de portagem:
 * - A1 (sistema fechado, cabines) — paga-se à saída, conforme a distância.
 * - A22 (SCUT, pórticos) — valor fixo por pórtico, `idEntrada == idSaida`.
 *
 * `object`: não faz sentido teres mais do que uma instância disto — é só
 * uma fonte de dados estática, não guarda estado próprio que mude.
 */
object MockPortagensRepository : PortagensRepository {

    private val classes = listOf(
        Classe(idClasse = 1, classe = "Classe 1"),
        Classe(idClasse = 2, classe = "Classe 2"),
        Classe(idClasse = 3, classe = "Classe 3"),
        Classe(idClasse = 4, classe = "Classe 4"),
        Classe(idClasse = 5, classe = "Motociclos")
    )

    private val autoestradas = listOf(
        Autoestrada(idAutoestrada = 1, nome = "A1 — Autoestrada do Norte"),
        Autoestrada(idAutoestrada = 2, nome = "A22 — Via do Infante")
    )

    private val portagensA1 = listOf(
        // Alverca e Pombal: coordenadas confirmadas no Google Maps (o nó da
        // A1 em cada localidade) — já não são a aproximação inicial.
        Portagem(idPortagem = 1, nome = "Lisboa (Alverca)", latitude = 38.89207901664875, longitude = -9.048095869682665, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 2, nome = "Pombal", latitude = 39.94016667, longitude = -8.67472222, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        // Coordenada do "Nó do Freixo" (nó da A1/A43 em Campanhã, Porto),
        // confirmada no OpenStreetMap via geocoding — não é aproximação.
        Portagem(idPortagem = 3, nome = "Porto (Freixo)", latitude = 41.1478, longitude = -8.5808, tipo = TipoPortagem.CABINE, idAutoestrada = 1)
    )

    private val portagensA22 = listOf(
        Portagem(idPortagem = 4, nome = "Tavira", latitude = 37.1256, longitude = -7.6497, tipo = TipoPortagem.PORTICO, idAutoestrada = 2),
        Portagem(idPortagem = 5, nome = "Faro", latitude = 37.0298, longitude = -7.9425, tipo = TipoPortagem.PORTICO, idAutoestrada = 2)
    )

    private val portagens = portagensA1 + portagensA22

    private val tarifas = listOf(
        // A1 — Classe 1 (preço cresce com a distância entre cabines)
        Tarifa(idTarifa = 1, preco = 6.20, idClasse = 1, idEntrada = 1, idSaida = 2),
        Tarifa(idTarifa = 2, preco = 12.40, idClasse = 1, idEntrada = 1, idSaida = 3),
        Tarifa(idTarifa = 3, preco = 9.30, idClasse = 1, idEntrada = 2, idSaida = 3),
        // A1 — Classe 2
        Tarifa(idTarifa = 4, preco = 9.90, idClasse = 2, idEntrada = 1, idSaida = 2),
        Tarifa(idTarifa = 5, preco = 18.60, idClasse = 2, idEntrada = 1, idSaida = 3),
        // A22 (SCUT) — idEntrada == idSaida: valor fixo por pórtico, não há "distância"
        Tarifa(idTarifa = 6, preco = 1.10, idClasse = 1, idEntrada = 4, idSaida = 4),
        Tarifa(idTarifa = 7, preco = 1.05, idClasse = 1, idEntrada = 5, idSaida = 5),
        Tarifa(idTarifa = 8, preco = 1.75, idClasse = 2, idEntrada = 4, idSaida = 4),
        Tarifa(idTarifa = 9, preco = 1.70, idClasse = 2, idEntrada = 5, idSaida = 5)
    )

    override fun listarClasses(): List<Classe> = classes
    override fun listarAutoestradas(): List<Autoestrada> = autoestradas
    override fun listarPortagens(): List<Portagem> = portagens
    override fun listarTarifas(): List<Tarifa> = tarifas
}
