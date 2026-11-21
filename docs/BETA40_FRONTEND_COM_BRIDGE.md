# ANIME HOJE PLAY — Beta 4.0 experimental

Gerada localmente uma versão com frontend próprio AMOLED (Home, catálogo, lançamentos, favoritos, programação em hora de Porto Velho, mangás e ajustes). O frontend é exibido em WebView local sobre a atividade React Native do APK, sem substituir o bundle Hermes do app.

## Integração implementada no código

- Um `AHPFrontendOverlay.install(Activity)` é chamado ao retomar a `TomatoReactive`.
- O frontend se comunica com uma ponte nativa, que usa a sessão já existente localmente para consultar rotas do serviço original: `GET /v2/animes/feed`, `POST /v2/content/search`, `GET /v2/user/favorites`, `GET /v2/anime/{id}`, `POST /v2/anime/recents` e `GET /v2/manga/feed`.
- A programação é consultada separadamente no AniList, com conversão de horário para `America/Porto_Velho`.
- O acesso ao app original fica disponível o tempo inteiro; detalhes de anime tentam abrir o deep link original `/watch?id=`.
- O token é utilizado apenas na ponte nativa e não exposto ao JavaScript.
- A nova interface só aparece depois de carregar o arquivo local; em caso de erro há fallback ao app original.
- O frontend é experimental: formas de resposta das APIs, recuperação do histórico e os deep links do player ainda não foram testados com uma sessão real.

## Validação realizada

- Código Java compilado para DEX.
- Integridade ZIP e assinaturas APK v2/v3 validadas.
- Apenas `classes.dex` modificado; `classes10.dex` e quatro assets foram adicionados.
- Bundle Hermes original, manifesto, bibliotecas e player nativo preservados.
- Testes automatizados da interface com respostas simuladas: navegação, busca, detalhes, agenda e fallback passaram sem erros JavaScript.

## Pendente antes de chamar versão final

Testes em Android autenticado: retorno do feed, detalhes, favoritos, busca, histórico, player e sessão; confirmar que o servidor permite os requests do novo frontend. Não confundir a compilação com integração ponta a ponta validada.

APK modificado e certificados privados **não são publicados** neste repositório público. O pacote de código criado separadamente contém somente o frontend novo.
