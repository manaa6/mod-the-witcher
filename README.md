# Bestiário do Bruxo (Fabric 1.20.1 + GeckoLib)

Conteúdo: 5 monstros animados (Afogador, Necrófago, Nekker, Grifo, Lobisomem), Espada de Aço, Espada de Prata
(dano extra contra monstros e mortos-vivos) e os 5 Sinais (Aard, Igni, Quen, Yrden, Axii) usados com clique direito.

## Como compilar
1. Instale o JDK 17 e o IntelliJ IDEA (ou use o Gradle 8.x pelo terminal).
2. Abra esta pasta no IntelliJ como projeto Gradle e aguarde baixar as dependências.
3. Para gerar o .jar: `gradle build` (o arquivo sai em `build/libs/bruxo-bestiario-1.0.0.jar`).
   Para testar direto no jogo: `gradle runClient`.
4. Na instalação normal do Minecraft 1.20.1 com Fabric Loader, coloque na pasta `mods`: o .jar do mod,
   o **Fabric API** e o **GeckoLib** (versão Fabric 1.20.1, 4.4.x).

## Testar
- Aba "Ovos de geração" do criativo, ou `/summon bruxo_bestiario:drowner` (ghoul, nekker, griffin, werewolf).
- `/give @s bruxo_bestiario:silver_sword`, `/give @s bruxo_bestiario:sign_igni` etc.

## Ajustar os modelos
Os modelos ficam em `src/main/resources/assets/bruxo_bestiario/geo/entity/`, as animações em `animations/entity/`
e as texturas em `textures/entity/`. Abra no Blockbench (plugin GeckoLib) para pintar e refinar.
Se alguma animação mexer para o lado errado, inverta o sinal dos valores de rotação no arquivo `.animation.json`.
