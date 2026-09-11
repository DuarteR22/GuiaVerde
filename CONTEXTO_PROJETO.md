# Contexto: Guia Verde (app Android)

Isto é um resumo do estado atual do projeto "Guia Verde", para dar contexto a
outra IA (ou a mim próprio, daqui a umas semanas). Cola isto no início da
conversa antes de pedires o que precisas.

## O que é a app

App Android nativa que calcula o custo exato de portagens em Portugal para
uma viagem: o utilizador escolhe Origem, Destino e Classe de veículo
(Classe 1, 2, 3, 4, Motociclos), a app cruza o trajeto com uma base de dados
de portagens e devolve o custo total. Cobre os dois sistemas portugueses:

- **Sistema fechado** (ex: A1): cabines de entrada e saída, paga-se conforme
  a distância percorrida entre elas.
- **Sistema aberto / ex-SCUT** (ex: A22): pórticos virtuais, valor fixo por
  passagem. Na modelação, isto representa-se com `idEntrada == idSaida` na
  tarifa correspondente.

A integração real com uma API de rotas (ex: OpenRouteService) e o cálculo
real de portagens ainda **NÃO foram feitos** — é o último passo do roteiro,
deliberadamente adiado até fechar toda a parte visual da app. Tudo o que
existe hoje usa dados mock (fixos, escritos à mão) ou conteúdo de referência
estático.

**Estado atual: toda a UI dos 3 ecrãs principais + os 3 separadores da
barra de navegação está completa e funcional, e o fluxo de cálculo
(Início → A calcular rota → Resumo) já usa `ViewModel` + `StateFlow` em
vez de estado espalhado pelos Composables. Falta só a parte de
backend/cálculo real (Passo 12).**

## Stack e decisões técnicas

- **Kotlin + Jetpack Compose**, app Android nativa, **um único módulo** `app`.
  (Foi ponderado Kotlin Multiplatform para partilhar UI com iOS, mas foi
  decidido simplificar e focar só em Android — não há Mac disponível para
  testar iOS.)
- Package raiz: `com.guiaverde.app`
- Gradle: Kotlin DSL + **version catalog** (`gradle/libs.versions.toml`).
  Kotlin 2.1.0, AGP 8.7.3, compose-bom 2024.12.01, compileSdk/targetSdk 35,
  minSdk 24.
- **Sem `kotlin { jvmToolchain(17) }`** — usa-se
  `compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }` em vez disso, porque
  o ambiente de desenvolvimento só tem o JBR (JetBrains Runtime) do Android
  Studio, sem um JDK 17 instalado à parte.
- **Sem core library desugaring ativado** (minSdk 24) — por isso o projeto
  evita deliberadamente `java.time.*`; datas guardam-se já formatadas como
  `String` (ex.: `ViagemHistorico.dataFormatada`).
- Navigation Compose (`androidx.navigation:navigation-compose:2.8.4`) —
  rotas simples em `String`, sem o sistema "type-safe" com
  `@Serializable` (não valeu a pena trazer o plugin kotlinx.serialization só
  para 3 rotas).
- Dependência extra: `androidx.compose.material:material-icons-extended`
  (o pacote "core" só tem ~30 ícones comuns; o design usa muitos mais).
- Gradle wrapper (`gradlew`/`gradlew.bat`) foi criado automaticamente pelo
  Android Studio (não foi escrito à mão).
- Ambiente Windows, Android Studio, sem Mac (só emulador Android).

## Design

O design visual foi feito no **Google Stitch**, exportado em dois lotes
(primeiro: Início/A calcular/Resumo; segundo: Histórico/Informações), e
está tudo mesclado em `design-export/` (HTML/Tailwind de referência +
screenshots + `DESIGN.md` com o design system completo). É só referência
visual: todo o código Compose foi escrito à mão a partir daí, não gerado.

- **Paleta**: Material 3, tons de verde (`primary #005B34`,
  `primaryContainer #137547`, `secondary #2C694E`, etc.) — replicada em
  `ui/theme/Color.kt` e ligada aos "roles" M3 em `ui/theme/Theme.kt` via
  `lightColorScheme(...)`. Só existe tema claro (o design não definiu dark
  mode).
- **Tipografia**: **Chivo** (destinos, autoestradas, valores monetários) e
  **Plus Jakarta Sans** (resto da UI), ambas *variable fonts* do Google
  Fonts (peso via `FontVariation.weight(x)`, precisa de
  `@OptIn(ExperimentalTextApi::class)`). Ficheiros em
  `app/src/main/res/font/`, licença OFL.
- Um punhado de cores fora do tema M3 existe deliberadamente em
  `SeccaoMetodosPagamento.kt` (âmbar/azul para alertas de prazo e tags
  informativas) — documentado no próprio ficheiro como exceção consciente,
  para bater certo com o design original.

## Modelo de dados (domínio)

Baseado num diagrama ER fornecido em JSON. Fica em `domain/model/`, espelha
a base de dados relacional pensada para o projeto — com a exceção de
`ResumoViagem`/`TrocoAutoestrada` (resultados de cálculo, não tabelas) e
`ViagemHistorico` (registo de viagem passada do utilizador):

```kotlin
data class Autoestrada(val idAutoestrada: Long, val nome: String)

data class Classe(val idClasse: Long, val classe: String) // "classe", não "descricao" — alinhado ao diagrama ER

enum class TipoPortagem { CABINE, PORTICO }

data class Portagem(
    val idPortagem: Long,
    val nome: String,
    val latitude: Double,
    val longitude: Double,
    val tipo: TipoPortagem,
    val idAutoestrada: Long
)

data class Tarifa(
    val idTarifa: Long,
    val preco: Double, // Double por simplicidade no MVP; considerar BigDecimal se processar pagamentos a sério
    val idClasse: Long,
    val idEntrada: Long,
    val idSaida: Long // == idEntrada para pórticos SCUT
)

data class TrocoAutoestrada(/* um troço percorrido, custo parcial, tipo de portagem, etc. */)

data class ResumoViagem(
    /* custo total, distância, duração, lista de TrocoAutoestrada, ... */
    // numeroPracasFisicas / numeroPorticos são propriedades COMPUTADAS
    // (troços.filter{...}.sumOf{...}), não campos guardados
)

data class ViagemHistorico(
    val id: Long,
    val origem: String,
    val destino: String,
    val custo: Double,
    val dataFormatada: String, // String pronta, não LocalDateTime (ver nota sobre desugaring acima)
    val favorita: Boolean,
    // ...
)
```

**Repositório** (`domain/repository/PortagensRepository.kt`): interface —
`listarClasses()`, `listarAutoestradas()`, `listarPortagens()`,
`listarTarifas()`. Implementação única hoje: `data/mock/MockPortagensRepository.kt`
(`object`, dados fixos cobrindo A1 fechada + A22 SCUT). Padrão *repository*
deliberado: a UI só depende da interface, para no futuro trocar por
Room/API sem tocar nos ecrãs.

Outros dados em `data/mock/`:
- `MockRotasFrequentes.kt` — resumos já calculados para a secção "Rotas
  Frequentes" do ecrã inicial.
- `MockResumoViagem.kt` — `gerar(classe: Classe): ResumoViagem`, usado pelo
  ecrã de Resumo.
- `MockHistoricoViagens.kt` — lista de `ViagemHistorico` + funções que
  **calculam** totais a partir da lista (`custoTotalMes()`,
  `totalQuilometragemMes()`), não valores hardcoded.

**Importante — distinção arquitetural usada no projeto todo:**
- `domain/model` = entidades que espelham a BD, ou resultados de cálculo
  genuínos/persistíveis.
- `data/mock` = dados falsos que simulam uma fonte de dados futura.
- Conteúdo de referência estático e real (texto legal, FAQ, tabela de
  classes do IMT, métodos de pagamento) **não é mock nem domínio** — vive
  direto dentro dos ficheiros de componente em `ui/components/informacoes/`,
  porque não há nada para "simular": é texto real que só vai mudar por
  edição manual.

## Estrutura de ficheiros atual

```
frontend/
├── README.md                     # roteiro/progresso passo a passo, estado exato sempre atualizado
├── CONTEXTO_PROJETO.md           # este ficheiro
├── design-export/                # export do Google Stitch (HTML + screenshots + DESIGN.md), 2 lotes mesclados
├── THIRD_PARTY_LICENSES_*.txt    # licenças OFL das fontes
├── gradle/libs.versions.toml
├── build.gradle.kts / settings.gradle.kts / gradle.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/font/                          # chivo_variable.ttf, plus_jakarta_sans_variable.ttf
        └── kotlin/com/guiaverde/app/
            ├── MainActivity.kt                # só chama GuiaVerdeApp()
            ├── domain/
            │   ├── model/                     # Autoestrada, Classe, Portagem, Tarifa, TipoPortagem,
            │   │                               # TrocoAutoestrada, ResumoViagem, ViagemHistorico
            │   └── repository/                # PortagensRepository (interface)
            ├── data/mock/                     # MockPortagensRepository, MockRotasFrequentes,
            │                                   # MockResumoViagem, MockHistoricoViagens
            └── ui/
                ├── GuiaVerdeApp.kt             # raiz: tema + Scaffold + barra de navegação inferior
                ├── theme/                      # Color.kt, Font.kt, Theme.kt (Material 3)
                ├── format/                     # Formatadores.kt (paraEuros, paraDuracao, comUmaCasaDecimal)
                ├── components/                 # TODOS os componentes, organizados por subpasta
                │   ├── comuns/                 # usados por mais do que um ecrã
                │   ├── inicio/                 # só usados pelo ecrã Início
                │   ├── resumo/                 # só usados pelo ecrã Resumo
                │   ├── historico/              # só usados pelo ecrã Histórico
                │   └── informacoes/            # só usados pelo ecrã Informações
                ├── navigation/                 # Ecra.kt (rotas), GuiaVerdeNavHost.kt
                └── screens/                    # um ficheiro por ecrã, sem subpastas
                    ├── InicioScreen.kt
                    ├── ACalcularRotaScreen.kt
                    ├── ResumoViagemScreen.kt
                    ├── HistoricoScreen.kt
                    └── InformacoesScreen.kt
```

**Componentes reutilizáveis mais importantes** (`ui/components/comuns/`):
- `TopoGuiaVerde` — barra do topo genérica, já na 3ª geração de parâmetros
  opcionais: `titulo`, `subtitulo: String?` (mostra Column de 2 linhas se
  presente), `onPesquisarClick: (() -> Unit)?` (mostra ícone de pesquisa se
  presente), `onAjudaClick`. Usado por Início, A calcular, Histórico e
  Informações.
- `BotaoAcaoPrimaria` / `BotaoAcaoSecundaria` — CTAs reutilizáveis com
  sombra verde customizada (`Modifier.shadow` com `ambientColor`/`spotColor`).
- `BarraNavegacaoInferior` + `enum class DestinoNavegacao { CALCULAR,
  HISTORICO, INFORMACOES }` — `NavigationBar`/`NavigationBarItem` do M3.

**Navegação** (`ui/navigation/`):
- `Ecra` — sealed class com as 3 rotas do fluxo de cálculo (`Inicio`,
  `ACalcularRota`, `ResumoViagem`). Chama-se "Ecra" e não "Destino" de
  propósito, para não colidir com o campo `destino` do formulário nem com o
  enum `DestinoNavegacao`.
- `GuiaVerdeNavHost` — liga os 3 ecrãs do fluxo com Navigation Compose;
  também é o dono do estado `origem`/`destino`/`classeSelecionadaId`
  (elevado até aqui porque tanto o Início como o A-calcular precisam dele).
- Há **dois níveis de navegação** que não se devem confundir: a barra
  inferior troca **secções** (`GuiaVerdeApp.kt`, um `when` simples sobre
  `DestinoNavegacao`), o `GuiaVerdeNavHost` gere o **fluxo dentro da secção
  "Calcular"** (Início → A calcular → Resumo).

## Roteiro / progresso

- [x] Passo 1 — Esqueleto Gradle + Jetpack Compose.
- [x] Passo 2 — Modelos de domínio.
- [x] Passo 3 — Tema Material 3 (cores + tipografia).
- [x] Passo 4 — Dados mock + repositório.
- [x] Passo 5 — Ecrã "Início e Cálculo" completo.
- [x] Passo 6 — Navigation Compose a ligar os 3 ecrãs do fluxo de cálculo.
- [x] Passo 7 — Ecrã "A calcular rota" a sério (animação de progresso
      simulada com `LaunchedEffect` + `delay`, avança sozinho ao fim de
      ~6.5s — o Passo 11 substitui isto por uma chamada de rede real).
- [x] Passo 8 — Ecrã "Resumo da Viagem" a sério (custo total, troços por
      autoestrada, combustível estimado).
- [x] Passo 9 — Ecrã "Histórico de Viagens" (filtros, favoritos, resumo
      mensal calculado, banner de relatório).
- [x] Passo 10 — Ecrã "Informações e Tarifários" (banner de isenção
      ex-SCUT, classes de veículos, métodos de pagamento, concessionárias,
      FAQ em acordeão). **A app já não tem nenhum ecrã placeholder.**
- [x] Passo 11 — Migração do estado do fluxo de cálculo (Início → A
      calcular rota → Resumo) para `ViewModel` + `StateFlow`
      (`CalculoViagemViewModel`, em `ui/viewmodel/`), substituindo o
      `remember` que antes vivia espalhado por `GuiaVerdeNavHost` e
      `InicioScreen`. Os ecrãs continuam sem lógica de estado própria —
      só recebem valores + callbacks.
- [ ] **Passo 12 — Integração com API de rotas real (ex: OpenRouteService)
      + cálculo real de portagens a partir do trajeto devolvido.**
      - [x] Autocompletar de Origem/Destino com geocoding REAL (API pública
        do Photon/OpenStreetMap, sem chave, restrita a Portugal por
        `bbox`). Primeira chamada de rede a sério da app — Retrofit +
        Gson (mínimo, sem mais nada por cima), com *debounce* de 300ms e
        mínimo de 3 caracteres no `CalculoViagemViewModel`. Modelos novos:
        `Coordenadas`/`SugestaoLocal` (domínio), `GeocodingRepository`
        (contrato) + `PhotonGeocodingRepository` (`data/remote/`, DTOs
        `internal`, nunca expostas fora do repositório).
      - [x] Trajeto real + deteção geográfica de portagens, SEM preços
        ainda: `RotaRepository`/`OsrmRotaRepository` (OSRM, servidor
        público sem chave, limite de 1 pedido/segundo) devolvem a rota
        como `List<Coordenadas>`; `Coordenadas.distanciaEmMetrosAte`
        (Haversine) + `detetarPortagensAtravessadas` (função pura em
        `domain/`) cruzam isso com o `PortagensRepository` e devolvem as
        portagens a menos de 80m do trajeto, na ordem certa, sem repetir
        a mesma seguida. Orquestrado por
        `CalculoViagemViewModel.calcularTrajeto()` (a parte de CPU corre
        em `Dispatchers.Default`); mostrado provisoriamente em texto no
        ecrã de Resumo.
      - [ ] Preços/tarifas a partir das portagens detetadas, por classe de
        veículo — próxima fase, ainda por fazer.

## Convenções do projeto (importante para manter consistência)

- **Nomes de variáveis, funções e comentários em português.** Nomes de
  tipos/pacotes Android seguem inglês só onde é API do próprio Android/Compose.
- **State hoisting em todo o lado**: nenhum componente de UI
  (`ui/components`) guarda o seu próprio estado a não ser que seja
  puramente visual e local (ex.: um accordion a abrir/fechar) — recebe
  valores + callbacks `on*Change`/`on*Click` de quem o chama. Os ecrãs
  (`ui/screens`) são donos do estado partilhado
  (`remember { mutableStateOf(...) }`), e o estado só é elevado até ao
  ancestral comum mais próximo que precisa dele — não preemptivamente até
  ao topo.
- **Nunca referenciar cores cruas nos ecrãs** — sempre
  `MaterialTheme.colorScheme.<role>`, nunca hex direto fora de `ui/theme/`
  (com a exceção documentada em `SeccaoMetodosPagamento.kt`, ver secção
  Design acima).
- **Separação domínio vs mock vs conteúdo estático**: ver secção "Modelo de
  dados" acima — é uma distinção de 3 vias, não só domínio-vs-mock.
- Slot APIs (parâmetros `@Composable () -> Unit`) usados quando um
  componente precisa de secções de conteúdo que variam por instância — ex.:
  `CartaoMetodoPagamento(conteudoExtra: (@Composable ColumnScope.() -> Unit)?)`.
- Documentação extensa em KDoc explicando o "porquê" das decisões — o
  projeto serve também para o autor aprender Kotlin/Compose a sério, por
  isso o código tem mais comentários pedagógicos do que um projeto de
  produção normal teria.

## O que ainda NÃO existe (para não presumir)

- Nenhuma base de dados real (Room ou outra) — só mock em memória e
  conteúdo estático nos componentes.
- Nenhum PREÇO real de portagens — a deteção geográfica (que portagens a
  rota atravessa) já é real (OSRM + Haversine), mas o `ResumoViagem`
  mostrado (custos, troços com valor) continua a vir do `MockResumoViagem`;
  ainda não há nenhuma ligação entre as duas coisas.
- `ViewModel` existe só para o fluxo de cálculo (`CalculoViagemViewModel`).
  Histórico e Informações continuam com estado local em `remember` — ainda
  não precisaram de ser partilhados com mais nenhum ecrã, por isso não
  houve razão para lhes dar o mesmo tratamento (é reavaliado se/quando
  precisarem, mesma regra de "eleva o estado só até onde é preciso").
- Sem testes automatizados.
- Sem dark theme (o design original não definiu um).
- Sem suporte a iOS (módulo Android único; o domínio foi pensado de forma
  portável, mas a decisão explícita foi não perseguir KMP por agora).
- Nenhuma persistência real do histórico de viagens ou dos favoritos (é
  tudo estado em memória, perdido ao fechar a app).

## Para onde perguntas futuras provavelmente vão apontar

Isto ainda não foi feito, mas é provável que apareça em conversas
seguintes: como estruturar a chamada à API de rotas, como cruzar a
polyline/trajeto devolvido com as coordenadas de `Portagem` para decider
que praças/pórticos foram atravessados, se vale a pena introduzir
`ViewModel`/`StateFlow` nessa altura, e como persistir o histórico de
viagens (Room, DataStore, ou API própria).
