# Keycloak - AulaBOH

Este componente deja preparada la autenticación centralizada del sistema AulaBOH.
Para no romper la demostración funcional actual, Keycloak queda como componente de plataforma y seguridad preparado para integración con JWT.

## Levantar Keycloak

Desde esta carpeta:

```powershell
cd "C:\proyectos\AulaBOH.v0\platform\keycloak"
docker compose up -d
```

Abrir:

```text
http://localhost:8089
```

Credenciales de administración:

```text
usuario: admin
contraseña: admin
```

## Realm importado

El archivo `realms/aulaboh-realm.json` crea:

- Realm: `aulaboh`
- Cliente público: `aulaboh-frontend`
- Cliente confidencial: `aulaboh-bff`
- Roles: `ADMIN`, `DOCENTE`, `ESTUDIANTE`, `APODERADO`
- Usuarios demo:
  - `admin.aulaboh` / `Admin123`
  - `docente.demo` / `Docente123`

## Uso en la defensa

Keycloak se presenta como componente de seguridad para manejo de sesiones, autenticación y roles. En esta versión queda preparado para integración futura, sin bloquear la demo funcional del frontend y microservicios.
