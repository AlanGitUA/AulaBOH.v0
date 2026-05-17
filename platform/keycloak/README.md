# Keycloak - AulaBOH

Este componente provee la base de autenticación centralizada para AulaBOH mediante realms, clientes, roles y usuarios iniciales.

## Levantar Keycloak

Desde esta carpeta:

```powershell
cd platform/keycloak
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
- Usuarios iniciales:
  - `admin.aulaboh` / `Admin123`
  - `docente.demo` / `Docente123`

## Uso esperado

Keycloak centraliza la administración de sesiones, autenticación y roles de usuario. Los clientes `aulaboh-frontend` y `aulaboh-bff` permiten separar el acceso de aplicaciones frontend y backend dentro del mismo realm.
