# Diretriz obrigatória — redesign sem alterar o fluxo do Tomato

Decisão do usuário em 18/11/2026: **o aplicativo deve continuar funcionando exatamente como antes; mudar somente a aparência**.

## Não negociável

- Preservar a rota original `SplashActivity` → autenticação → `HomePageActivity`.
- Preservar login (inclusive Google), catálogo real, busca, progresso de reprodução, favoritos, mangás, histórico, notificações e player.
- Não transformar `PreviewActivity` em `MAIN/LAUNCHER` do APK Tomato.
- A interface `AHPHomeView` é um **protótipo** com `Host` incompleto; não usá-la para substituir a Home até que todos os seus dados sejam realmente fornecidos pelo controlador original e todos os fluxos tenham passado em testes.
- Não exibir placeholders como “Os títulos aparecerão após conectar o catálogo original” em uma build destinada ao uso normal.
- Manter IDs, callbacks, endpoints, persistência, assinaturas de classes e recursos funcionais. Nenhuma alteração em DEX ou bundle Hermes sem necessidade comprovada.
- Nunca publicar APKs originais/modificados, credenciais ou certificados em repositório público.

## Método aprovado

1. Criar um baseline da última build em que **catálogo real e player funcionavam** (Beta 2.5, relatada por captura).
2. Aplicar mudanças visuais em dimensões, cores, logos, tipografia, cantos e organização dos componentes existentes.
3. Para mudanças estruturais maiores, mapear controladores reais, adaptar somente a camada de visualização sem desconectar os serviços nem perder a navegação.
4. Comparar hashes DEX/SO/Hermes/manifest e recurso de navegação com o baseline.
5. Passar testes no dispositivo: entrada, login Google, catálogo, busca, episódio, continuar assistindo, mangás, histórico, favoritos, perfil e player.
6. Criar APK de produção apenas depois desses testes. Não chamar compilação de validação funcional.

## Limitação atual

A Beta 2.8 voltou à navegação nativa e preserva o código executável do baseline; contém refinamentos visuais, **não** a substituição completa do layout. A aba Programação está implementada em protótipo separado, mas ainda não integra o fluxo original. Não anunciar funções adicionais como prontas.
