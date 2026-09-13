# Guia Verde

App para calcular o custo exato de portagens em Portugal para uma viagem
(Origem, Destino, Classe de veículo), cruzando o trajeto com uma base de
dados de portagens (sistema fechado com cabines de entrada/saída, e
sistema aberto/SCUT de pórticos).

## Stack

- **Kotlin** + **Jetpack Compose** — app Android nativa, um único módulo.
  (Ponderámos Kotlin Multiplatform para partilhar UI com iOS, mas decidimos
  simplificar e focar só em Android por agora.)

