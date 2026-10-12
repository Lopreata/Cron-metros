# Cron-metros 📅

App Android nativo (Java) para contar dias com temáticas personalizadas — dias dormindo bem, dias sem recaída, dias se exercitando, e muito mais.

## 📥 Download do APK

**Não precisa instalar nada.** O APK é gerado automaticamente pelo GitHub Actions a cada atualização.

👉 **[Clique aqui para baixar o APK mais recente](../../releases/latest)**

📱 **Link direto para o celular:**
```
https://github.com/Lopreata/Cron-metros/releases/download/latest-build/cron-metros.apk
```

> Para instalar: ative **"Fontes desconhecidas"** (ou "Instalar apps desconhecidos") nas configurações do Android, abra o `.apk` e instale.

---

## Funcionalidades

- **Múltiplos cronômetros** simultâneos com tema, emoji e cor personalizados
- **Contagem de dias** com exibição de horas e minutos
- **Pausar / Retomar** individualmente ou em lote via checkbox
- **Zerar** com confirmação, individual ou em lote
- **Seleção múltipla** para ações em lote (pausar, retomar, zerar, excluir)
- **Dados salvos localmente** — persistem mesmo com o app fechado
- **Atualiza ao reabrir** — contagem recalculada automaticamente
- Tema escuro (Catppuccin Mocha)

## Como funciona o build automático

A cada push, o GitHub Actions:
1. Compila o APK com Gradle (Java nativo, sem Node.js)
2. Publica na aba **Releases** como `latest-build`

## Desenvolvimento local

Pré-requisitos: **Java 17** e **Android SDK** instalados.

```bash
# Clonar o repositório
git clone https://github.com/Lopreata/Cron-metros.git
cd Cron-metros

# Compilar APK de debug
./gradlew assembleDebug

# O APK fica em:
# app/build/outputs/apk/debug/app-debug.apk
```
