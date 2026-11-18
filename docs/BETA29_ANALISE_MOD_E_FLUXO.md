# Beta 2.9 — correção pontual do popup sem alterar fluxo

**Status: APK experimental compilado localmente; não publicado no GitHub; sem teste Android.**

## Diagnóstico

A versão externa do Tomato contém 23 classes injetadas no `classes.dex`, incluindo `com.facebook.ads.zzdbca`, cujo código monta um diálogo promocional customizado. A chamada `zzdbca.get(this)` foi encontrada em `com.tomatos.clientapp.react.TomatoReactive.onResume()`; **a chamada não está no APK interno recuperado**.

A classe `FenixZIN` também aparece na camada de assinatura e **não** foi removida, porque não é a rotina que desenha o diálogo e excluí-la poderia interferir na inicialização.

## Solução prioritária — Beta 2.9 mínima

Em vez de reconstruir toda a camada de inicialização, a Beta 2.9 foi compilada **sobre a Beta 2.8 funcional** com uma única instrução de 6 bytes neutralizada em `TomatoReactive.onResume()`. Os bytes do método mudaram de `77 01 90 5c 01 00` para `00 00 00 00 00 00`, mantendo as chamadas ao ciclo de vida React Native.

- Única entrada ZIP modificada: `classes.dex` (inclui checksums internos).
- Manifesto, Hermes, bibliotecas nativas, identificadores, recursos visuais, endpoints, login, player, catálogo, favoritos e histórico mantidos **byte por byte**.
- Certificado de assinatura das Betas 2.x usado; integridade ZIP e esquema de assinatura v2 verificados.
- **Não houve teste em dispositivo**, portanto a ausência visual do popup e a estabilidade ainda exigem confirmação.

## O que ainda não foi alterado

- O aviso "Atualize seu app"; existe uma checagem real de versão e status em `SplashActivity.checkUpdate` conectada ao servidor, que também participa da inicialização. Não desativá-la sem validação.
- A aparência pixel a pixel do mockup AMOLED: necessita refazer os componentes com os mesmos dados/callbacks originais, sem usar a `PreviewActivity` desconectada.

## Como validar

Teste atualização por cima, sem desinstalar ou limpar dados, com backup. Verifique login Google, catálogo, busca, player, progresso, favoritos, histórico, mangás e perfil. Se falhar, capture `adb logcat -d -v time -s AndroidRuntime:E`.

**Não subir APK modificado, cópia do código proprietário, chave privada ou credenciais neste repositório público.**
