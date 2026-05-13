# Frontend AulaBOH

Frontend React/Vite para la plataforma de libro de clases digital.

## Patrón demostrado

- **Module Pattern / Component Pattern:** separación en `components`, `pages` y `services`.
- **Consumo de BFF:** la interfaz consume `/api/bff`, evitando llamadas directas a todos los microservicios.
- **Componente NPM reutilizable:** usa `@aulaboh/frontend-components` desde `packages/frontend-components`.

## Ejecutar

```bash
npm install
npm run dev
```

URL: `http://localhost:5173`
