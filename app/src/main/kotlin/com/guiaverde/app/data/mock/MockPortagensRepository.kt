package com.guiaverde.app.data.mock

import com.guiaverde.app.domain.model.Autoestrada
import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.Tarifa
import com.guiaverde.app.domain.model.TipoPortagem
import com.guiaverde.app.domain.repository.PortagensRepository

/**
 * Implementação "falsa" do repositório: `classes` e `tarifas` continuam
 * escritas à mão (preços fictícios, para termos algo a mostrar antes do
 * Passo 12 fase 2 — cálculo de preços reais). `autoestradas` e
 * `portagens`, a partir do Passo 12, já são coordenadas REAIS — extraídas
 * de um export Overpass (OpenStreetMap) de nós `barrier=toll_booth` /
 * `highway=toll_gantry` em Portugal, não inventadas.
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
        Autoestrada(idAutoestrada = 2, nome = "A22 — Via do Infante"),
        Autoestrada(idAutoestrada = 3, nome = "A3 — Autoestrada do Minho"),
        Autoestrada(idAutoestrada = 4, nome = "A8 — Autoestrada do Oeste"),
        Autoestrada(idAutoestrada = 5, nome = "A10 — Autoestrada do Norte / Ponte Vasco da Gama"),
        Autoestrada(idAutoestrada = 6, nome = "A11 — Autoestrada Braga/Guimarães"),
        Autoestrada(idAutoestrada = 7, nome = "A13 — Autoestrada do Pinhal Interior"),
        Autoestrada(idAutoestrada = 8, nome = "A17 — Autoestrada do Litoral Centro"),
        Autoestrada(idAutoestrada = 9, nome = "A21 — Autoestrada de Mafra")
    )

    // Pontos do A1 CONFIRMADOS contra o trajeto real Lisboa→Porto devolvido
    // pelo OSRM (não só "existem no OSM" — testámos que a rota passa mesmo
    // perto de cada um). Alverca e Pombal vieram de pins dados à mão pelo
    // utilizador no Google Maps; "Coimbra" e "Feira" não têm nome no OSM
    // (só coordenadas), daí o nome ser a localidade mais próxima, não o
    // nome oficial da portagem.
    private val portagensA1 = listOf(
        Portagem(idPortagem = 1, nome = "Lisboa (Alverca)", latitude = 38.89207901664875, longitude = -9.048095869682665, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 2, nome = "Pombal", latitude = 39.94016667, longitude = -8.67472222, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 3, nome = "Feira", latitude = 41.0170191, longitude = -8.5815395, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 6, nome = "Torres Novas", latitude = 39.478768, longitude = -8.627292, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 7, nome = "Condeixa", latitude = 40.131146, longitude = -8.492341, tipo = TipoPortagem.CABINE, idAutoestrada = 1),
        Portagem(idPortagem = 8, nome = "Coimbra", latitude = 40.2026451, longitude = -8.488521, tipo = TipoPortagem.CABINE, idAutoestrada = 1)
    )

    private val portagensA22 = listOf(
        // A22 não tem nenhuma entrada nomeada no export do OSM que usámos
        // — Tavira/Faro continuam por confirmar contra uma rota real
        // (ainda não testámos nenhum trajeto no Algarve).
        Portagem(idPortagem = 4, nome = "Tavira", latitude = 37.1256, longitude = -7.6497, tipo = TipoPortagem.PORTICO, idAutoestrada = 2),
        Portagem(idPortagem = 5, nome = "Faro", latitude = 37.0298, longitude = -7.9425, tipo = TipoPortagem.PORTICO, idAutoestrada = 2)
    )

    // Portagens de outras autoestradas — reais (OSM, nome+coordenadas
    // verdadeiros), mas SÓ o A1 foi confirmado contra uma rota a sério; a
    // atribuição à autoestrada certa vem do próprio nome do local no OSM
    // (ex: "Braga Oeste (A3)"), não de um teste de trajeto.
    private val portagensOutras = listOf(
        // A3 — Autoestrada do Minho
        Portagem(idPortagem = 9, nome = "Anais - Vila Verde", latitude = 41.690681, longitude = -8.534654, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 10, nome = "Braga Oeste", latitude = 41.537588, longitude = -8.498049, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 11, nome = "Braga Sul", latitude = 41.507903, longitude = -8.463729, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 12, nome = "Cruz", latitude = 41.456461, longitude = -8.490987, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 13, nome = "Ponte de Lima Norte", latitude = 41.779411, longitude = -8.557324, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 14, nome = "Ponte de Lima S. Norte", latitude = 41.769722, longitude = -8.561719, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        Portagem(idPortagem = 15, nome = "Ponte de Lima S. Sul", latitude = 41.771754, longitude = -8.556537, tipo = TipoPortagem.CABINE, idAutoestrada = 3),
        // A8 — Autoestrada do Oeste
        Portagem(idPortagem = 16, nome = "Campelos", latitude = 39.197351, longitude = -9.200111, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 17, nome = "Enxara", latitude = 38.98824, longitude = -9.213666, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 18, nome = "Loures", latitude = 38.843208, longitude = -9.168049, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 19, nome = "Lousa", latitude = 38.904122, longitude = -9.20595, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 20, nome = "Marinha Grande Este", latitude = 39.744357, longitude = -8.875682, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 21, nome = "Pataias", latitude = 39.640606, longitude = -8.966757, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 22, nome = "Ramalhal", latitude = 39.128065, longitude = -9.252044, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 23, nome = "Torres Vedras N", latitude = 39.107877, longitude = -9.246189, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        Portagem(idPortagem = 24, nome = "Torres Vedras S", latitude = 39.061683, longitude = -9.242507, tipo = TipoPortagem.CABINE, idAutoestrada = 4),
        // A10 — liga à Ponte Vasco da Gama
        Portagem(idPortagem = 25, nome = "Benavente Norte", latitude = 38.95854, longitude = -8.824646, tipo = TipoPortagem.CABINE, idAutoestrada = 5),
        // A11 — Braga/Guimarães
        Portagem(idPortagem = 26, nome = "Ferreiros", latitude = 41.531523, longitude = -8.467957, tipo = TipoPortagem.CABINE, idAutoestrada = 6),
        // A13 — Pinhal Interior
        Portagem(idPortagem = 27, nome = "Santo Estevão", latitude = 38.840779, longitude = -8.727787, tipo = TipoPortagem.CABINE, idAutoestrada = 7),
        // A17 — Litoral Centro
        Portagem(idPortagem = 28, nome = "Leiria Norte", latitude = 39.81163, longitude = -8.842186, tipo = TipoPortagem.CABINE, idAutoestrada = 8),
        Portagem(idPortagem = 29, nome = "Monte Real", latitude = 39.853001, longitude = -8.832929, tipo = TipoPortagem.CABINE, idAutoestrada = 8),
        Portagem(idPortagem = 30, nome = "Monte Redondo", latitude = 39.893232, longitude = -8.810815, tipo = TipoPortagem.CABINE, idAutoestrada = 8),
        // A21 — Mafra
        Portagem(idPortagem = 31, nome = "Venda do Pinheiro", latitude = 38.928413, longitude = -9.219851, tipo = TipoPortagem.CABINE, idAutoestrada = 9)
    )

    private val portagens = portagensA1 + portagensA22 + portagensOutras

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
        // Sem tarifas para as portagens novas (9-31) — preços a sério são
        // a fase seguinte do Passo 12; esta fase é só deteção geográfica.
    )

    override fun listarClasses(): List<Classe> = classes
    override fun listarAutoestradas(): List<Autoestrada> = autoestradas
    override fun listarPortagens(): List<Portagem> = portagens
    override fun listarTarifas(): List<Tarifa> = tarifas
}
