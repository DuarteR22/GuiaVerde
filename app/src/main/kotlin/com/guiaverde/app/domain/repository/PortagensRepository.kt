package com.guiaverde.app.domain.repository

import com.guiaverde.app.domain.model.Autoestrada
import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.Tarifa

/**
 * Contrato de acesso aos dados de portagens. A UI (e o resto do domínio)
 * só vai conhecer esta interface — nunca a implementação concreta. Hoje
 * a única implementação é [com.guiaverde.app.data.mock.MockPortagensRepository]
 * (uma lista fixa em memória); mais tarde substitui-se por uma que lê de
 * Room e/ou de uma API, sem mudar uma linha sequer nos ecrãs que a usam.
 * A este princípio chama-se "inversão de dependência".
 */
interface PortagensRepository {
    fun listarClasses(): List<Classe>
    fun listarAutoestradas(): List<Autoestrada>
    fun listarPortagens(): List<Portagem>
    fun listarTarifas(): List<Tarifa>
}
