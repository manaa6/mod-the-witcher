# The Witcher: Swords & Signs (Fabric 1.20.1)

## Itens
- **Barra de Prata**: ferro + quartzo (sem forma)
- **Espada de Aço**: 2 ferro + couro (vertical) — bônus contra saqueadores/bruxas
- **Espada de Prata**: 2 barras de prata + couro — bônus contra monstros
- **Aerondight**: 3 barras de prata + barra de netherite + couro (formato de cruz) — bônus enorme contra monstros

## Sinais (teclas configuráveis em Controles > The Witcher)
- `X` próximo sinal, `C` sinal anterior, `Z` lançar
- Aard: onda de choque | Igni: cone de fogo | Quen: escudo (absorção)
- Yrden: armadilha no chão (lentidão + brilho) | Axii: controla a mente do mob na mira

## Como compilar
1. Instale o JDK 17.
2. Baixe o template em https://fabricmc.net/develop/template/ (1.20.1) e copie de lá
   a pasta `gradle/`, e os arquivos `gradlew` e `gradlew.bat` para esta pasta
   (ou, tendo Gradle 8.x instalado: `gradle wrapper --gradle-version 8.5`).
3. Rode `./gradlew build` (Windows: `gradlew.bat build`).
4. O mod fica em `build/libs/witcher-mod-1.0.0.jar`. Coloque em `.minecraft/mods` com o Fabric Loader e o Fabric API 0.92.2+1.20.1.
