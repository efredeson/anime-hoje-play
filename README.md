# ANIME HOJE PLAY — Interface AMOLED 3.1

Projeto de desenvolvimento da **nova interface Android do ANIME HOJE PLAY**, com fundo preto AMOLED, roxo sólido, navegação simplificada e calendário de estreias no horário de Porto Velho.

**Status:** implementação de interface em desenvolvimento. O APK de prévia é independente e **não substitui** o aplicativo Tomato original. Catálogo, login, favoritos, histórico e player originais ainda não estão conectados.

## Organização
- `app/`: APK Android de prévia para testar a nova interface sem afetar a instalação atual.
- `ui/`: módulo de interface Android (.aar) para integração posterior.
- `.github/workflows/build-android.yml`: compilação Android automática pelo GitHub Actions.
- `docs/INTEGRACAO_TOMATO.md`: requisitos técnicos para conectar os dados e o player originais.

## Compilar no GitHub
1. Acesse a aba **Actions**.
2. Abra **ANIME HOJE PLAY — Android build** e selecione **Run workflow**.
3. Após a compilação, baixe os artefatos **ANIME-HOJE-PLAY-Preview-Debug** e **ANIME-HOJE-PLAY-UI-Library**.

A compilação produz o app de **prévia** e a biblioteca Android, não uma modificação funcional completa do Tomato.

## Segurança e direitos
- **Não adicione APKs originais ou modificados do Tomato, segredos, senhas ou chaves de assinatura** a este repositório.
- Este repositório está **público** atualmente. Se quiser proteger o código, altere-o para **Private** em **Settings → General → Danger Zone → Change repository visibility**.
- Use imagens e conteúdo audiovisual somente com as autorizações necessárias.

O objetivo é conectar o catálogo original com segurança e validar a nova interface em Android antes de substituir a Home do app que já funciona.
