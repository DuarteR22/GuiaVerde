# Guia Verde

App para calcular o custo exato de portagens em Portugal para uma viagem
(Origem, Destino, Classe de veículo), cruzando o trajeto com uma base de
dados de portagens (sistema fechado com cabines de entrada/saída, e
sistema aberto/SCUT de pórticos).

## Stack

- **Kotlin** + **Jetpack Compose** — app Android nativa, um único módulo.
  (Ponderámos Kotlin Multiplatform para partilhar UI com iOS, mas decidimos
  simplificar e focar só em Android por agora.)

## Design

O design visual (cores, tipografia, componentes) foi feito no Google Stitch
e está em [`design-export/`](design-export/) — screenshots + HTML de
referência + [`DESIGN.md`](design-export/stitch_guia_verde_toll_calculator/guia_verde/DESIGN.md)
com o design system completo. Usa as fontes **Chivo** e **Plus Jakarta
Sans** (Google Fonts, licença OFL — ver `THIRD_PARTY_LICENSES_*.txt` na
raiz), incluídas como *variable fonts* em `app/src/main/res/font/`.

## Estrutura do projeto

```
frontend/
└── app/
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/font/              # Chivo e Plus Jakarta Sans (variable fonts)
        └── kotlin/com/guiaverde/app/
            ├── MainActivity.kt        # arranque da app, só chama GuiaVerdeApp()
            ├── domain/
            │   ├── DetetorPortagens.kt # função pura: cruza rota + Portagem (Haversine, 80m)
            │   ├── model/              # Autoestrada, Classe, Portagem, Tarifa, TipoPortagem,
            │   │                       # TrocoAutoestrada, ResumoViagem, ViagemHistorico,
            │   │                       # Coordenadas (+ distanciaEmMetrosAte), SugestaoLocal
            │   └── repository/         # PortagensRepository, GeocodingRepository, RotaRepository
            ├── data/
            │   ├── mock/               # MockPortagensRepository, MockRotasFrequentes,
            │   │                       # MockResumoViagem, MockHistoricoViagens
            │   └── remote/             # PhotonApi/PhotonGeocodingRepository (geocoding) +
            │                           # OsrmApi/OsrmRotaRepository (trajeto real) — DTOs internal
            └── ui/
                ├── GuiaVerdeApp.kt     # raiz: tema + barra de navegação + troca de separador
                ├── theme/              # Color.kt, Font.kt, Theme.kt (Material 3)
                ├── format/             # Formatadores.kt (paraEuros, paraDuracao, ...)
                ├── components/             # TODOS os componentes, organizados por pasta
                │   ├── comuns/             # usados por mais do que um ecrã (botões, topo, nav bar)
                │   ├── inicio/             # só usados pelo ecrã Início (OrigemDestinoCard, ...)
                │   ├── resumo/             # só usados pelo ecrã Resumo (CartaoCustoTotal, ...)
                │   ├── historico/          # só usados pelo ecrã Histórico (CartaoViagem, ...)
                │   └── informacoes/        # só usados pelo ecrã Informações (SeccaoFaq, ...)
                ├── navigation/             # Ecra.kt (rotas) + GuiaVerdeNavHost.kt
                ├── viewmodel/              # CalculoViagemViewModel (estado do fluxo de cálculo)
                └── screens/                # um ficheiro por ecrã, sem subpastas (só há 1 ficheiro por ecrã)
                    ├── InicioScreen.kt
                    ├── ACalcularRotaScreen.kt
                    ├── ResumoViagemScreen.kt
                    ├── HistoricoScreen.kt
                    └── InformacoesScreen.kt
```

## Como correr (Android Studio)

1. Abrir esta pasta (`frontend/`) no Android Studio.
2. Se aparecer um aviso a dizer que falta o Gradle wrapper, aceitar a
   sugestão do Android Studio para o criar — é normal, este scaffold foi
   escrito à mão sem o wrapper binário.
3. Esperar pelo Gradle sync.
4. Escolher a configuração `app` e correr no emulador.

## Progresso

- [x] **Passo 1** — Esqueleto Gradle + Jetpack Compose num único módulo `app`,
      a renderizar um ecrã simples.
- [x] **Passo 2** — Modelos de domínio (`Autoestrada`, `Classe`, `Portagem`,
      `Tarifa`, `TipoPortagem`) em `domain/model`, espelhando a base de dados.
- [x] **Passo 3** — Tema Material 3 (`ui/theme/`): cores e tipografia
      (Chivo + Plus Jakarta Sans, variable fonts) extraídas do design Stitch.
- [x] **Passo 4** — Dados mock (`data/mock`) + `PortagensRepository`
      (`domain/repository`), cobrindo os dois sistemas (A1 fechado, A22 SCUT).
- [x] **Passo 5** — Ecrã "Início e Cálculo" (formulário Origem/Destino/Classe).
      - [x] Cabeçalho + cartão Origem/Destino (`OrigemDestinoCard`, state hoisting)
      - [x] Chips de Classe do Veículo (`ClasseVeiculoSelector`, `FilterChip` + `LazyRow`)
      - [x] Switch Via Verde + checkbox "Evitar portagens" (`PreferenciasCard`)
      - [x] Botão "Calcular Portagens" (`BotaoAcaoPrimaria`, reutilizável)
      - [x] Lista "Rotas Frequentes" (`RotasFrequentesSection` + `MockRotasFrequentes`)
      - [x] Barra de navegação inferior (`BarraNavegacaoInferior`, `NavigationBar`)
- [x] **Passo 6** — Navigation Compose (`ui/navigation`): `Ecra` (rotas) +
      `GuiaVerdeNavHost` a ligar Início → A calcular → Resumo, com placeholders
      para os dois últimos (conteúdo real nos Passos 7 e 8).
- [x] **Passo 7** — Ecrã "A calcular rota" a sério: resumo da viagem,
      cartão de progresso animado (`LaunchedEffect`, `Crossfade`,
      `rememberInfiniteTransition`) e cartão de dica. Avança sozinho para o
      Resumo ao fim de alguns segundos (simulação — o Passo 9 substitui o
      `delay()` por uma chamada de rede real).
- [x] **Passo 8** — Ecrã "Resumo da Viagem" a sério: cartão de custo total
      com métricas rápidas, discriminação por autoestrada (cores/badges
      conforme `TipoPortagem`), combustível estimado, e os botões "Ver
      Detalhes e Pórticos" / "Nova Pesquisa". Modelos novos:
      `ResumoViagem` e `TrocoAutoestrada` (domínio) + `MockResumoViagem`.
- [x] **Passo 9** — Ecrã "Histórico de Viagens": chips de filtro (Todas /
      Favoritas, funcionais; Este Mês / Filtros, visuais por agora), cartão
      de resumo mensal (totais **calculados** a partir da lista, não
      hardcoded), lista de viagens com favoritos (toggle com `.copy()`) e
      banner de relatório. Modelo novo: `ViagemHistorico` (domínio) +
      `MockHistoricoViagens`. `TopoGuiaVerde` generalizado (título +
      pesquisa opcional) para servir este ecrã também.
- [x] **Passo 10** — Ecrã "Informações e Tarifários": banner de isenção
      ex-SCUT (gradiente + `FlowRow`), grelha 2×2 de classes de veículos,
      3 cartões de métodos de pagamento (com um "slot" `conteudoExtra`
      para as partes que variam entre eles), lista de concessionárias, e
      FAQ em acordeão (`animateContentSize`, `animateFloatAsState`,
      cada pergunta expande/recolhe de forma independente). Sem modelos
      novos em `domain/` — isto é conteúdo de referência real (a lei, as
      regras do IMT), não dados calculados nem mock, por isso vive direto
      nos componentes de `ui/components/informacoes/`.
      **A app já não tem nenhum ecrã placeholder** — os 3 separadores da
      barra de navegação têm todos conteúdo real.
- [x] **Passo 11** — Migração do estado do fluxo de cálculo (Início → A
      calcular rota → Resumo) de `remember` espalhado pelos Composables
      para `ViewModel` + `StateFlow`: `CalculoViagemViewModel` (novo,
      `ui/viewmodel/`) passa a ser o único dono de `origem`, `destino`,
      `classeSelecionadaId`, `viaVerdeAtivo` e `evitarPortagens`, obtido
      uma vez em `GuiaVerdeNavHost` com `viewModel()` e lido com
      `collectAsStateWithLifecycle()`. Os ecrãs continuam "burros" (só
      recebem valores + callbacks `on*`) — só mudou QUEM guarda o estado,
      não a estrutura visual. Efeito lateral positivo: a viagem agora
      sobrevive a rodar o ecrã e a trocar de separador na barra inferior
      (antes, com `remember`, perdia-se).
- [ ] **Passo 12 — Integração com API de rotas + cálculo real de portagens.**
      - [x] Autocompletar de Origem/Destino com geocoding real: API pública
        do Photon (OpenStreetMap, sem chave), restrita a Portugal por
        `bbox`. `GeocodingRepository` (domínio) + `PhotonGeocodingRepository`
        (`data/remote/`, Retrofit + Gson) traduzem o GeoJSON para
        `SugestaoLocal`/`Coordenadas` (domínio, novos). No
        `CalculoViagemViewModel`: pesquisa com *debounce* de 300ms, mínimo
        de 3 caracteres, e `flatMapLatest` para cancelar pedidos
        ultrapassados; as sugestões aparecem numa lista por baixo de cada
        campo (`OrigemDestinoCard`), e escolher uma guarda nome +
        coordenadas no `uiState`. Tipos de local sem interesse para uma
        viagem de carro (limites administrativos — distrito/concelho/país
        — e paragens de transporte público) são filtrados por `osm_value`
        antes de chegarem a sugestão — é o que evita, por exemplo, ver
        "Faro" repetido 3 vezes (cidade + concelho + distrito, o mesmo
        nome para 3 entidades OSM diferentes); `distinctBy` fica como rede
        de segurança para o que ainda assim sobrar igual.
      - [x] Trajeto real + deteção geográfica de portagens (sem preços
        ainda): `RotaRepository` (domínio) + `OsrmRotaRepository`
        (`data/remote/`, servidor público do OSRM — sem chave, limite de
        1 pedido/segundo, uso não-comercial) devolvem a geometria da rota
        como `List<Coordenadas>`. `Coordenadas.distanciaEmMetrosAte`
        (Haversine) + `detetarPortagensAtravessadas` (`domain/`, função
        pura) cruzam os pontos da rota com o `PortagensRepository` e
        devolvem as portagens a menos de 80m, pela ordem do trajeto, sem
        repetir a mesma seguida. `CalculoViagemViewModel.calcularTrajeto()`
        orquestra tudo (a parte de CPU corre em `Dispatchers.Default`);
        `ACalcularRotaScreen` deixou de simular um `delay()` fixo — quem
        decide quando o cálculo termina é este trabalho real, disparado
        num `LaunchedEffect` no `GuiaVerdeNavHost`. Resultado mostrado
        (provisório, só texto) numa secção nova no ecrã de Resumo.
      - [ ] Preços/tarifas a partir das portagens detetadas, por classe de
        veículo — próxima fase.
