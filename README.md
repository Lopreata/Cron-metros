# Cron-metros 📅

App Android para contar dias com temáticas personalizadas — dias dormindo bem, dias sem recaída, dias se exercitando, etc.

## Funcionalidades

- **Múltiplos cronômetros** simultaneamente, cada um com tema, emoji e cor personalizados
- **Contagem de dias** com exibição de horas e minutos
- **Pausar / Retomar** cada cronômetro individualmente ou em lote
- **Zerar** com confirmação individual ou em lote
- **Seleção múltipla** via checkbox para ações em lote (pausar, retomar, zerar, excluir)
- **Persistência local** — os dados ficam salvos no dispositivo mesmo com o app fechado
- **Atualiza ao reabrir** — ao voltar ao app, a contagem é recalculada automaticamente
- Tema escuro (Catppuccin Mocha)

## Gerar o APK

### Opção 1 — Expo EAS Build (recomendado, sem precisar de Android Studio)

```bash
# 1. Instale o EAS CLI
npm install -g eas-cli

# 2. Faça login na sua conta Expo
eas login

# 3. Configure o projeto (primeira vez)
eas build:configure

# 4. Gere o APK
eas build -p android --profile preview
```

O link para download do APK aparece no terminal ao final do build (~5–10 min).

### Opção 2 — Build local com Android Studio

```bash
# 1. Gere o projeto Android nativo
npx expo prebuild --platform android

# 2. Abra a pasta `android/` no Android Studio e gere o APK via Build > Build APK(s)
```

### Opção 3 — Testar no celular agora (sem build)

```bash
npm start
# Escaneie o QR Code com o app Expo Go no celular
```

## Desenvolvimento

```bash
npm install
npm run android   # emulador Android
npm start         # Expo Go
```
