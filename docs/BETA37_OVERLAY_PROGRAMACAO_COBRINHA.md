# Beta 3.7 — Botão flutuante de programação e cobrinha

**Status:** compilação local experimental, ainda não validada em dispositivo.

## Estratégia de integração

Utilizar a Beta 3.5, que mostrou o catálogo original funcionando, como base. Conservar **byte a byte** o bundle Hermes, o manifesto e as bibliotecas nativas. Adicionar um novo DEX contendo `AHPScheduleOverlay` e introduzir somente a chamada `AHPScheduleOverlay.install(this)` no final de `TomatoReactive.onResume()`.

Não substituir a `HomePageActivity` nem a navegação do React Native. Não usar `PreviewActivity` como Home.

## Recursos

- Botão flutuante roxo na área interna da atividade React Native.
- Painel nativo com agenda de sete dias, títulos e episódios da API pública AniList GraphQL.
- Conversão pelo fuso `America/Porto_Velho` (UTC−4). Horário de transmissão previsto **não garante** disponibilidade do título no catálogo Tomato.
- Animação de cobrinha com segmentos roxos em uma faixa decorativa sem capturar toques, desligável no painel. Esta implementação inicial não segue cada card individualmente e pode aparecer em outras telas da mesma atividade React Native.

## Comparação estática

Duas entradas distintas em relação ao APK de base:
- `classes.dex`: reserializado com troca de apenas uma classe de atividade contendo a chamada ao painel;
- `classes10.dex`: novo DEX com as classes da extensão.

**Todos os demais arquivos preservados.** ZIP e certificado APK v2/v3 verificados. Sem testes em Android; assinatura íntegra não garante que o overlay será exibido ou que não haverá fechamento.

Não publicar APK do Tomato, chave de assinatura nem credenciais neste repositório público. Os arquivos de fonte da extensão estão disponíveis em pacote separado no chat.
