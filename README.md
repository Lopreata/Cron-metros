# Cron-metros 📅

App Android para contar dias com temáticas personalizadas — dias dormindo bem, dias sem recaída, dias se exercitando, e muito mais.

## 📥 Download do APK

**Não precisa instalar nada.** O APK é gerado automaticamente pelo GitHub Actions a cada atualização.

👉 **[Clique aqui para baixar o APK mais recente](../../releases/latest)**

> Para instalar: ative **"Fontes desconhecidas"** (ou "Instalar apps desconhecidos") nas configurações do Android, abra o `.apk` e instale.

---

## Funcionalidades

- **Múltiplos cronômetros** simultâneos com tema, emoji e cor personalizados
- **Contagem de dias** com exibição de horas e minutos
- **Pausar / Retomar** individualmente ou em lote via checkbox
- **Zerar** com confirmação, individual ou em lote
- **Seleção múltipla** para ações em lote (pausar, retomar, zerar, excluir)
- **Dados salvos localmente** — persistem mesmo com o app fechado
- **Atualiza ao reabrir** — contagem recalculada automaticamente em segundo plano
- Tema escuro

## Como funciona o build automático

A cada push, o GitHub Actions:
1. Gera o projeto Android nativo via `expo prebuild`
2. Compila o APK com Gradle
3. Publica na aba **Releases** como `latest-build`

## Desenvolvimento local

```bash
npm install
npm start         # Expo Go (escanear QR code no celular)
npm run android   # emulador Android
```
