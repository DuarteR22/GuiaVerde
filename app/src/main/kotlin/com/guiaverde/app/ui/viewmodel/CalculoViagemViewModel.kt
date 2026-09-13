package com.guiaverde.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guiaverde.app.data.mock.MockPortagensRepository
import com.guiaverde.app.data.mock.MockResumoViagem
import com.guiaverde.app.data.mock.RotaFrequente
import com.guiaverde.app.data.remote.OsrmRotaRepository
import com.guiaverde.app.data.remote.PhotonGeocodingRepository
import com.guiaverde.app.domain.calcularCustoPortagens
import com.guiaverde.app.domain.detetarPortagensAtravessadas
import com.guiaverde.app.domain.model.Classe
import com.guiaverde.app.domain.model.Coordenadas
import com.guiaverde.app.domain.model.Portagem
import com.guiaverde.app.domain.model.ResumoViagem
import com.guiaverde.app.domain.model.SegmentoPreco
import com.guiaverde.app.domain.model.SugestaoLocal
import com.guiaverde.app.domain.repository.GeocodingRepository
import com.guiaverde.app.domain.repository.PortagensRepository
import com.guiaverde.app.domain.repository.RotaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * Valor usado quando não há (ainda) nenhuma viagem escolhida — vazio de
 * propósito, para o campo Origem mostrar só o placeholder ("Ex: Lisboa ou
 * Porto") ao abrir a app, em vez de vir pré-preenchido.
 */
private const val ORIGEM_OMISSAO = ""
private const val ID_CLASSE_OMISSAO = 1L

/** Regras do autocompletar de Origem/Destino (Passo 12). */
private const val MIN_CARACTERES_PESQUISA = 3
private const val DEBOUNCE_PESQUISA_MS = 300L

/**
 * Estado observável do fluxo de cálculo (Início → A calcular rota →
 * Resumo). Um único `data class` imutável — cada evento do
 * [CalculoViagemViewModel] produz uma cópia nova via `.copy()`, nunca
 * muta esta em memória.
 *
 * [classes] vive aqui (não em cada ecrã) porque mais do que um ecrã
 * precisa de resolver [classeSelecionadaId] para a [Classe] completa —
 * uma única leitura ao repositório, feita pelo ViewModel, em vez de cada
 * ecrã ler por si.
 */
data class CalculoViagemUiState(
    val origem: String = ORIGEM_OMISSAO,
    val destino: String = "",
    // Só ficam preenchidas depois de o utilizador escolher uma sugestão —
    // editar o texto à mão volta a limpá-las (ver onOrigemChange/onDestinoChange).
    val origemCoordenadas: Coordenadas? = null,
    val destinoCoordenadas: Coordenadas? = null,
    val sugestoesOrigem: List<SugestaoLocal> = emptyList(),
    val sugestoesDestino: List<SugestaoLocal> = emptyList(),
    // Preenchida por `calcularTrajeto()` — resultado real (OSRM +
    // deteção de interseção), não mock. Ver KDoc de `calcularTrajeto`.
    val portagensDetetadas: List<Portagem> = emptyList(),
    // Preço por troço (Passo 12, fase 2) — calculado a partir de
    // portagensDetetadas + classeSelecionadaId; ver calcularTrajeto().
    val segmentosPreco: List<SegmentoPreco> = emptyList(),
    val classes: List<Classe> = emptyList(),
    val classeSelecionadaId: Long = ID_CLASSE_OMISSAO,
    val viaVerdeAtivo: Boolean = true,
    val evitarPortagens: Boolean = false
) {
    /**
     * A [Classe] atualmente escolhida — CALCULADA a partir de [classes] +
     * [classeSelecionadaId], nunca guardada à parte (mesmo raciocínio das
     * propriedades computadas em [ResumoViagem]): assim nunca podem ficar
     * dessincronizadas uma da outra.
     */
    val classeSelecionada: Classe?
        get() = classes.firstOrNull { it.idClasse == classeSelecionadaId }
}

/**
 * Dono do estado do fluxo de cálculo. Início, "A calcular rota" e Resumo
 * partilham a mesma instância — obtida uma única vez em
 * [com.guiaverde.app.ui.navigation.GuiaVerdeNavHost] com `viewModel()` —
 * em vez de cada um ter o seu próprio `remember`.
 *
 * Desde o Passo 12, também é o dono do autocompletar de Origem/Destino:
 * chama o [geocodingRepository] com debounce e guarda as sugestões e as
 * coordenadas escolhidas no [uiState]. Os ecrãs continuam "burros" — só
 * recebem valores + callbacks `on*`.
 *
 * `@JvmOverloads` no construtor: gera, por baixo, um construtor sem
 * argumentos — é isso que permite a `viewModel()` (que usa reflexão)
 * criar esta classe sem precisarmos de lhe dar uma `Factory` à mão. Em
 * testes, passam-se outras implementações dos repositórios aqui.
 */
class CalculoViagemViewModel @JvmOverloads constructor(
    private val portagensRepository: PortagensRepository = MockPortagensRepository,
    private val geocodingRepository: GeocodingRepository = PhotonGeocodingRepository,
    private val rotaRepository: RotaRepository = OsrmRotaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CalculoViagemUiState(classes = portagensRepository.listarClasses())
    )

    /** Só de leitura para fora — só o próprio ViewModel pode mudar [_uiState]. */
    val uiState: StateFlow<CalculoViagemUiState> = _uiState.asStateFlow()

    // "Gatilhos" só para a PESQUISA de geocoding — deliberadamente
    // separados de `uiState.origem`/`uiState.destino`: escrever no campo
    // dispara-os (ver onOrigemChange), mas ESCOLHER uma sugestão
    // (onSugestaoOrigemSelecionada) não — senão o dropdown reabria
    // sozinho 300ms depois de o utilizador escolher, com as sugestões do
    // texto que ele próprio tinha acabado de aceitar.
    private val pesquisaOrigem = MutableStateFlow("")
    private val pesquisaDestino = MutableStateFlow("")

    init {
        observarPesquisa(pesquisaOrigem) { sugestoes -> _uiState.update { it.copy(sugestoesOrigem = sugestoes) } }
        observarPesquisa(pesquisaDestino) { sugestoes -> _uiState.update { it.copy(sugestoesDestino = sugestoes) } }
    }

    fun onOrigemChange(valor: String) {
        // Limpa a coordenada guardada: se o texto mudou à mão, o par
        // lat/long que lá estava já não corresponde ao que se vê no campo.
        _uiState.update { it.copy(origem = valor, origemCoordenadas = null) }
        pesquisaOrigem.value = valor
    }

    fun onDestinoChange(valor: String) {
        _uiState.update { it.copy(destino = valor, destinoCoordenadas = null) }
        pesquisaDestino.value = valor
    }

    /** Troca origem ↔ destino, texto E coordenadas juntos — numa única atualização atómica. */
    fun onTrocarOrigemDestino() {
        _uiState.update {
            it.copy(
                origem = it.destino,
                destino = it.origem,
                origemCoordenadas = it.destinoCoordenadas,
                destinoCoordenadas = it.origemCoordenadas
            )
        }
    }

    fun onLimparDestino() {
        _uiState.update { it.copy(destino = "", destinoCoordenadas = null, sugestoesDestino = emptyList()) }
    }

    fun onClasseSelecionada(idClasse: Long) {
        _uiState.update { it.copy(classeSelecionadaId = idClasse) }
    }

    fun onViaVerdeChange(ativo: Boolean) {
        _uiState.update { it.copy(viaVerdeAtivo = ativo) }
    }

    fun onEvitarPortagensChange(ativo: Boolean) {
        _uiState.update { it.copy(evitarPortagens = ativo) }
    }

    /** Preenche origem/destino a partir de uma rota frequente escolhida — sem coordenadas (não vêm de geocoding). */
    fun onRotaFrequenteSelecionada(rota: RotaFrequente) {
        _uiState.update {
            it.copy(origem = rota.origem, destino = rota.destino, origemCoordenadas = null, destinoCoordenadas = null)
        }
    }

    /** Escolher uma sugestão da lista: preenche texto + coordenadas, e fecha o dropdown (lista fica vazia). */
    fun onSugestaoOrigemSelecionada(sugestao: SugestaoLocal) {
        _uiState.update {
            it.copy(origem = sugestao.nome, origemCoordenadas = sugestao.coordenadas, sugestoesOrigem = emptyList())
        }
    }

    fun onSugestaoDestinoSelecionada(sugestao: SugestaoLocal) {
        _uiState.update {
            it.copy(destino = sugestao.nome, destinoCoordenadas = sugestao.coordenadas, sugestoesDestino = emptyList())
        }
    }

    /**
     * Chamado a partir do Resumo com "Nova Pesquisa": limpa a viagem para
     * recomeçar do zero. Mantém [CalculoViagemUiState.viaVerdeAtivo] e
     * [CalculoViagemUiState.evitarPortagens] — são preferências do
     * utilizador, não da viagem, por isso não faria sentido perdê-las.
     */
    fun onNovaPesquisa() {
        _uiState.update {
            it.copy(
                origem = ORIGEM_OMISSAO,
                destino = "",
                origemCoordenadas = null,
                destinoCoordenadas = null,
                sugestoesOrigem = emptyList(),
                sugestoesDestino = emptyList(),
                portagensDetetadas = emptyList(),
                segmentosPreco = emptyList(),
                classeSelecionadaId = ID_CLASSE_OMISSAO
            )
        }
    }

    /**
     * Obtém o trajeto real (OSRM) entre a Origem e o Destino escolhidos,
     * deteta que portagens atravessa (Haversine, ver
     * [com.guiaverde.app.domain.detetarPortagensAtravessadas]) e calcula o
     * preço de cada troço para a [CalculoViagemUiState.classeSelecionadaId]
     * atual (ver [com.guiaverde.app.domain.calcularCustoPortagens]).
     *
     * `suspend`, não lançada com `viewModelScope`: quem chama isto — o
     * ecrã "A calcular rota", através de um `LaunchedEffect` no
     * [com.guiaverde.app.ui.navigation.GuiaVerdeNavHost] — é dono do
     * tempo de vida da chamada. Se o utilizador premir "Cancelar" a meio,
     * o `LaunchedEffect` sai de composição e esta coroutine cancela-se
     * sozinha, em vez de continuar em segundo plano.
     */
    suspend fun calcularTrajeto() {
        val estadoAtual = uiState.value
        val origemCoord = estadoAtual.origemCoordenadas
        val destinoCoord = estadoAtual.destinoCoordenadas

        if (origemCoord == null || destinoCoord == null) {
            // Ainda não escolheu uma sugestão do autocompletar para os
            // dois campos — sem coordenadas não há como pedir rota ao
            // OSRM. Obrigar a escolher uma sugestão antes de calcular é
            // validação de formulário, fora do âmbito desta fase — por
            // agora só evita chamar a API sem argumentos válidos.
            _uiState.update { it.copy(portagensDetetadas = emptyList(), segmentosPreco = emptyList()) }
            return
        }

        val pontosRota = rotaRepository.obterRota(origemCoord, destinoCoord)
        val portagensConhecidas = portagensRepository.listarPortagens()

        // Dispatchers.Default: é trabalho de CPU (Haversine ponto a
        // ponto), não de rede — não faz sentido correr na mesma
        // dispatcher usada para chamadas de rede nem, já agora, na thread
        // principal (ver a conversa sobre desempenho: isto é rápido de
        // sobra, mas continua a ser boa prática separar por tipo de trabalho).
        val (portagensAtravessadas, segmentosPreco) = withContext(Dispatchers.Default) {
            val atravessadas = detetarPortagensAtravessadas(pontosRota, portagensConhecidas)
            val precos = calcularCustoPortagens(
                atravessadas,
                estadoAtual.classeSelecionadaId,
                portagensRepository.listarTarifas(),
                portagensRepository.listarAutoestradas()
            )
            atravessadas to precos
        }

        _uiState.update { it.copy(portagensDetetadas = portagensAtravessadas, segmentosPreco = segmentosPreco) }
    }

    /**
     * Gera o resumo da viagem atualmente escolhida. Mock por agora (usa
     * [MockResumoViagem]) — mais à frente o corpo desta função passa a
     * fazer o cálculo real sobre o trajeto devolvido pela API de rotas
     * (que já vai poder usar `origemCoordenadas`/`destinoCoordenadas`); a
     * assinatura já não precisa de mudar, nem quem a chama.
     */
    fun gerarResumoViagem(): ResumoViagem {
        val classe = uiState.value.classeSelecionada ?: portagensRepository.listarClasses().first()
        return MockResumoViagem.gerar(classe)
    }

    /**
     * Liga um "gatilho" de texto (o que o utilizador escreveu) à pesquisa
     * de geocoding:
     * - `debounce`: só pesquisa quando parar de escrever durante
     *   [DEBOUNCE_PESQUISA_MS] — sem isto, cada tecla premida disparava
     *   um pedido HTTP.
     * - o mínimo de [MIN_CARACTERES_PESQUISA] evita pesquisas inúteis (e
     *   caras) com 1-2 letras.
     * - `flatMapLatest`: se chegar texto novo enquanto um pedido anterior
     *   ainda está no ar, cancela-o e fica só com o resultado do mais
     *   recente — nunca mostra sugestões de uma pesquisa já ultrapassada.
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observarPesquisa(gatilho: Flow<String>, aoObterSugestoes: (List<SugestaoLocal>) -> Unit) {
        gatilho
            .debounce(DEBOUNCE_PESQUISA_MS)
            .flatMapLatest { texto ->
                if (texto.trim().length < MIN_CARACTERES_PESQUISA) {
                    flowOf(emptyList<SugestaoLocal>())
                } else {
                    flow { emit(geocodingRepository.pesquisarLocais(texto)) }
                }
            }
            .onEach(aoObterSugestoes)
            .launchIn(viewModelScope)
    }
}
