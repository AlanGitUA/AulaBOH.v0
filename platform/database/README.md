# PostgreSQL

Infraestructura local de persistencia para los microservicios de dominio.

## Bases disponibles

| Base | Servicio |
|---|---|
| `aulaboh_students` | `students-service` |
| `aulaboh_attendance` | `attendance-service` |
| `aulaboh_grades` | `grades-service` |

## Inicio

```powershell
cd platform/database
docker compose up -d
```

## Estado

```powershell
docker compose ps
```

## Conexion

| Dato | Valor |
|---|---|
| Host | `localhost` |
| Puerto | `5432` |
| Usuario | `aulaboh` |
| Contrasena | `aulaboh` |

## Validacion rapida

```powershell
docker exec -it aulaboh-postgres psql -U aulaboh -d postgres -c "\l"
```

La inicializacion de las bases se ejecuta solo cuando el volumen de PostgreSQL se crea por primera vez.
