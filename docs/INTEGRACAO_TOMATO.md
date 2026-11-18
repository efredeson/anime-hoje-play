# Integração REAL com o Tomato — roteiro técnico

Este projeto compila uma interface AMOLED Android e um APK de **prévia independente**. Ainda **não modifica** o Tomato instalado.

## Componentes
- `AHPHomeView.Host`: interface que precisa ser implementada pelo controlador Android real.
- `AHPProgramacaoView`: agenda de transmissão pública consultada no AniList, convertida para **America/Porto_Velho (UTC−4)**.
- `PreviewActivity`: app de prévia para testar a interface. Não acessa login, favoritos, reprodução, histórico nem servidores originais.

## Integração pendente
1. Mapear a `HomePageActivity` e ações do catálogo, busca, histórico, favoritos e perfil no código original.
2. Criar um adaptador `AHPHomeView.Host` que encaminhe essas ações às rotas efetivas sem inventar APIs privadas.
3. Integrar o módulo Android compilado e a navegação às atividades existentes, em ambiente controlado.
4. Testar instalação, retorno do player, login, catálogo e agenda.
5. Somente então gerar a versão assinada para atualização.

Não coloque o APK do Tomato, material proprietário extraído, chaves `.p12`, senhas ou segredos no GitHub. O repositório atual é público.
