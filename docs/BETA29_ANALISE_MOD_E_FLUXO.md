# Beta 2.9 — Retirar popup mod sem desconectar o fluxo

**18/11/2026 — Diagnóstico estático e compilação experimental**

O pacote externo continha um arquivo interno `Fênix/origin.apk`, recuperado com CRC correto, com código prévio à camada externa de proteção/publicidade. Uma comparação de classes e do código de `TomatoReactive` encontrou:

- 23 classes adicionais no `classes.dex` externo sob `com.facebook.ads.*`, incluindo `zzdbca` que cria um `AlertDialog` customizado.
- Uma chamada adicional `zzdbca.get(this)` em `TomatoReactive.onResume()`, ausente da versão interna recuperada.
- A versão externa também trocou o nome da classe `Application` no manifesto para `fancybypass.component.FancyApplication` e adicionou `classes9.dex`.

A Beta 2.9 **foi compilada localmente** a partir da estrutura interna recuperada: mantém os oito DEX da base interna, o Hermes, as bibliotecas e as rotas originais; preserva 40 recursos de visual AMOLED/ANIME HOJE PLAY da Beta 2.8, sem adicionar a `PreviewActivity` desconectada. A assinatura APK v2 passou na verificação estática e usa o mesmo certificado das Betas 2.x.

**Não publicar o APK nem a chave privada neste repositório público.**

## Pendências
- Teste real no Android: confirmar abertura, login, catálogo real, player, mangás, favoritos, histórico, perfil e ausência do popup.
- O aviso "Atualize seu app" não foi removido. A `SplashActivity.checkUpdate` consulta o servidor e participa do fluxo de inicialização; desabilitá-la pode quebrar autenticação/compatibilidade.
- A reprodução exata do mockup AMOLED ainda não foi implementada nas telas nativas sem perda do fluxo; demanda vincular o novo desenho aos mesmos dados e callbacks que o app original usa.

Não anunciar a Beta 2.9 como versão final nem afirmar que removeu todos os avisos antes do teste em dispositivo.
