# Falha após login na Beta 3.2 — diagnóstico e recuperação

**Sintoma relatado:** após entrar na conta, a tela fica preta na Beta 3.2.

## Comparação dos APKs

- **Beta 3.0:** foi exibida em captura do usuário com catálogo e listas de animes reais carregando. A organização visual antiga permanece, mas a Home estava funcional naquele teste.
- **Beta 3.1:** passou a modificar o bytecode Hermes para alterar cores, tamanho da marca e altura do card.
- **Beta 3.2:** mudou mais oito funções do bundle Hermes, incluindo `HomeHeader`, `SectionAlert` e a renderização das seções, além da imagem horizontal do cabeçalho.
- Ao comparar Beta 3.0 vs. 3.2, as entradas que tiveram CRC alterado são apenas `assets/index.android.bundle` e `res/drawable-mdpi-v4/assets_images_tomato_icon.png` (ignorando assinatura META-INF da v1, removida pela assinatura v2/v3).
- `AndroidManifest.xml`, `resources.arsc` e arquivos DEX são byte a byte idênticos; ZIPs passaram em `testzip`.
- Certificados de assinatura v2/v3 de Beta 3.0, 3.1 e 3.2: **mesmo SHA-256**.

## Interpretação

O regressão é compatível com um erro JavaScript/Hermes na montagem da Home após login. Não é possível atribuir a uma função específica sem `logcat`; ter passado no `asm-check` não garante comportamento correto em runtime.

## Recuperação recomendada

1. **Não limpar dados nem desinstalar.**
2. Tentar instalar por cima a **Beta 3.0** já compilada, que usava o bundle anterior funcional; certificado igual, manifesto igual.
3. Se o Android impedir por versão inferior ou outro motivo, não forçar a desinstalação; capturar a mensagem exibida.
4. Se o problema continuar, coletar `adb logcat -c`, reproduzir falha e `adb logcat -d -v time -s ReactNativeJS:E AndroidRuntime:E ReactNative:E`.
5. Não lançar outra build Hermes não testada até diagnosticar a exceção.

**Nota:** este arquivo é um diagnóstico e não contém APK, informações de autenticação ou chaves privadas. O repositório é público.
