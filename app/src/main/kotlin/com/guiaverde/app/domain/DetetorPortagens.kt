package com.guiaverde.app.domain

import com.guiaverde.app.domain.model.Coordenadas
import com.guiaverde.app.domain.model.Portagem

/** Distância máxima (metros) a uma portagem para a considerarmos atravessada pela rota. */
private const val DISTANCIA_MAXIMA_METROS = 80.0

/**
 * Cruza os pontos de uma rota real com as [portagens] conhecidas e
 * devolve as que a rota atravessa, pela ORDEM em que acontecem ao longo
 * do trajeto, sem repetir a mesma portagem em pontos consecutivos — a
 * rota passa "a menos de 80m" da mesma cabine em várias dezenas de
 * pontos seguidos; sem esta verificação, apareceria repetida dezenas de
 * vezes seguidas na lista.
 *
 * Fase só GEOGRÁFICA (Passo 12, primeira fase): decide só QUE portagens
 * foram atravessadas — sem preços, sem diferenciar por classe de
 * veículo. Isso fica para a fase seguinte.
 *
 * É uma função de topo, não uma `class`/`object`: é pura (só depende dos
 * parâmetros, não lê nem guarda estado próprio), por isso não precisa de
 * ser instanciada — mesmo raciocínio das funções em `ui/format/Formatadores.kt`.
 * Não usa coroutines nem sabe em que thread corre; é o ViewModel que
 * decide correr isto fora da thread principal (`Dispatchers.Default`),
 * porque é trabalho de CPU, não de rede.
 */
fun detetarPortagensAtravessadas(pontosRota: List<Coordenadas>, portagens: List<Portagem>): List<Portagem> {
    val atravessadas = mutableListOf<Portagem>()

    for (ponto in pontosRota) {
        var maisProxima: Portagem? = null
        var menorDistancia = Double.MAX_VALUE

        for (portagem in portagens) {
            val distancia = portagem.coordenadas.distanciaEmMetrosAte(ponto)
            if (distancia < menorDistancia) {
                menorDistancia = distancia
                maisProxima = portagem
            }
        }

        if (maisProxima != null &&
            menorDistancia <= DISTANCIA_MAXIMA_METROS &&
            atravessadas.lastOrNull()?.idPortagem != maisProxima.idPortagem
        ) {
            atravessadas.add(maisProxima)
        }
    }

    return atravessadas
}

/**
 * Auxiliar de DIAGNÓSTICO (temporário, Passo 12 fase 1) — não é usado pela
 * deteção em si, só para perceber, durante os testes, a que distância
 * real cada [portagem] ficou do trajeto. Se uma portagem "devia" ser
 * detetada e não é, isto diz se está a 90m (afinar [DISTANCIA_MAXIMA_METROS]
 * pode chegar) ou a 5km (as coordenadas do mock estão erradas — problema
 * diferente). Devolve pela ordem, mais perto primeiro.
 */
fun distanciasMinimasPorPortagem(pontosRota: List<Coordenadas>, portagens: List<Portagem>): List<Pair<Portagem, Double>> {
    return portagens
        .map { portagem -> portagem to (pontosRota.minOfOrNull { portagem.coordenadas.distanciaEmMetrosAte(it) } ?: Double.NaN) }
        .sortedBy { (_, distancia) -> distancia }
}
