# Beta 3.2 — Mudanças na Home real do React Native

**Status:** APK experimental criado localmente e assinado, ainda **não testado em dispositivo**. O APK e a chave privada não são publicados neste repositório público.

## Arquivos modificados

Comparação com a Beta 3.1: apenas duas entradas do APK mudaram, além dos metadados da assinatura:

1. `assets/index.android.bundle` (Hermes v90) — funções de apresentação da Home;
2. `res/drawable-mdpi-v4/assets_images_tomato_icon.png` — arte horizontal centralizada, derivada da imagem aprovada.

## Mudanças em oito funções do Hermes

- `HomeHeader (9033)`: centralização da marca no cabeçalho, busca e perfil preservados.
- Estilo `tinyLogo (9551)`: área da marca ampliada para 145 × 49.
- `SectionAlert (9975)`: card de aviso da Home oculto, **sem desligar a verificação remota de versão**, mas **também ocultando os demais avisos exibidos por esse componente**.
- `HomeFeedMapper (9973)`: títulos das seções com tipografia menos pesada.
- `ItemDefault (10054)`: cards dos carrosséis ampliados para 148 unidades.
- `SectionTrending (10052)` e `SectionDefault (10035)`: alturas e espaçamentos da vitrine refinados.
- `heightByType (9966)`: altura correspondente ao conteúdo aumentada para não recortar.

## Proteções

Manifesto, DEX, SO, navegação do app, dados, login e player foram mantidos. Apenas uma parte do layout foi refinada, **não** a composição exata do mockup. A quinta aba **Programação** ainda não está integrada à Home original.

**Verificações:** ZIP íntegro, função Hermes compilada/asm-check e assinatura APK v2/v3 com o certificado da Beta 3.1. Isso **não substitui** o teste no celular.

**Atenção:** ocultar um banner de atualização não significa que o app está atualizado; alertas de servidor podem ser importantes. Se o serviço bloquear uma versão antiga, esconder o banner não resolve a incompatibilidade.

Não trocar a Home original pela `PreviewActivity`, para não perder catálogo, favoritos e histórico.
