# Beta 3.0 — Personalização incremental da Home original

A Beta 3.0 foi produzida localmente sobre a Beta 2.9, **sem publicar o APK no GitHub**. Esta abordagem substitui a tentativa anterior de usar a `PreviewActivity` como Home.

## Alterações exclusivamente visuais

53 atributos de estilo alterados em 11 arquivos Android XML compilados, mantendo dimensões estruturais, IDs e handlers:

- `home_page_toolbar.xml`: logo maior, hierarquia do nome e margens
- `bnv_tab_item_foreground.xml`: selecionado roxo `#9631D5`, outros ícones cinza `#A9A7B1`
- `home_recycler_featured.xml`, `home_recycler_1..4.xml`: títulos e margens
- `home_card_continuewatching.xml`: título, descrição, progresso, borda do botão
- `home_card_spotlight.xml`, `home_card_1.xml`, `home_card_2.xml`: cantos arredondados e ajustes discretos

## Preserve o fluxo

- Manifesto e nove DEX **idênticos byte por byte** aos da Beta 2.9.
- Bundle Hermes e bibliotecas `.so` intactos.
- `SplashActivity` e `HomePageActivity` seguem como o fluxo normal do app.
- Catálogo real, busca, reprodução, progresso, favoritos, histórico, login, mangás e perfil não recebem alterações de código.
- A aba Programação permanece **fora** do fluxo nativo enquanto não houver integração de dados e navegação verificada.

## Validação / limitações

- Assinatura Android v2 e v3 validada, mesmo certificado das Betas 2.x.
- Inspeção ZIP comparativa: 11 recursos visuais modificados, outros componentes preservados.
- **Ainda não testado no Android**. Não declarar final nem pixel a pixel igual ao mockup.
- Não contornar avisos de atualização real do servidor sem confirmar segurança/compatibilidade.

Não publicar APK modificado nem chave privada no repositório público.
