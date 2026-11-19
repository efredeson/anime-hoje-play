# Beta 3.1 — corrigir a tela certa (Hermes React Native)

A captura de 19/11/2026 comprovou que a Beta 3.0 manteve quase toda a Home anterior.

## Causa confirmada

A tela real foi rastreada por decompilação:
- `SplashActivity` inicia `com.tomatos.clientapp.react.TomatoReactive`;
- `TomatoReactive` inicia `ReactRootView` com o componente `TomatoNativeApp`;
- `assets/index.android.bundle` é um bundle Hermes **versão 90**;
- XMLs como `home_recycler_featured.xml` e `home_page_toolbar.xml` pertencem à estrutura nativa legada e não montam a Home exibida.

**Logo, editar esses XMLs não reproduz o layout aprovado.**

## Beta 3.1 experimental preparada localmente

Mudanças direcionadas ao Hermes:
- `tomatoColorDark`: `#101010` → `#000000` (AMOLED);
- `tomatoSemiDark`: `#1B1B1B` → `#111115`;
- `tomatoColor2`: `#e53935` → `#9631D5` (roxo sólido);
- ícones/rótulos ativos da navegação consultam `tomatoColor2`;
- logo real `tinyLogo`: `size(16)` → `size(28)`;
- card `SectionCardKeepWatching`: `moderateScale(195,0.6)` → `moderateScale(178,0.6)`.

**O APK local não foi publicado aqui**. Todos os DEX, o manifesto, `resources.arsc`, as bibliotecas nativas e as imagens ficaram idênticos; apenas o bundle Hermes foi substituído e a assinatura reemitida. O código modificado passou por análise e verificação sintática (`asm-check`), assinatura v2/v3 e alinhamento, **mas não foi testado no Android**.

Não anunciar a versão como idêntica ao mockup. Ainda faltam o banner com layout cinematográfico e a reorganização das seções dentro dos componentes originais, além da integração de Programação sem quebrar as rotas existentes. Não publicar APKs originais ou certificados no repositório público.
