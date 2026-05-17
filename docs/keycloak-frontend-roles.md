# Integración Frontend + Keycloak por roles

## Objetivo

El botón **Iniciar sesión** del sitio público redirige a Keycloak. Después del login, el frontend lee los roles del token y envía al usuario solo al panel permitido.

## Usuarios iniciales

| Usuario | Contraseña | Rol | Acceso esperado |
|---|---|---|---|
| admin.aulaboh | Admin123 | ADMIN | Panel, estudiantes, gestión académica y resumen académico |
| docente.demo | Docente123 | DOCENTE | Gestión académica y resumen académico |
| estudiante.demo | Estudiante123 | ESTUDIANTE | Resumen del estudiante asociado a la cuenta |
| apoderado.demo | Apoderado123 | APODERADO | Resumen de estudiantes asociados a la cuenta |

## Rutas protegidas

| Ruta | Roles permitidos |
|---|---|
| /panel | ADMIN, DOCENTE |
| /estudiantes | ADMIN |
| /gestion-academica | ADMIN, DOCENTE |
| /resumen-academico | ADMIN, DOCENTE, ESTUDIANTE, APODERADO |

## Flujo

1. Usuario presiona **Iniciar sesión**.
2. El frontend redirige a Keycloak.
3. Keycloak autentica al usuario.
4. Keycloak devuelve el token al frontend.
5. El frontend revisa los roles del token.
6. Se redirige al panel correspondiente.
7. Las rutas no permitidas envían a `/no-autorizado`.

## Comandos

Levantar Keycloak:

```powershell
cd platform/keycloak
docker compose up -d
```

Frontend:

```powershell
cd apps/frontend
npm install
npm run dev
```

Abrir:

```txt
http://localhost:5173
```
