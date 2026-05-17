# Persistencia de datos

## Objetivo

Mantener datos de estudiantes, asistencia y calificaciones entre reinicios de los servicios mediante PostgreSQL y migraciones versionadas con Flyway.

## Distribucion de bases

| Base | Servicio propietario |
|---|---|
| `aulaboh_students` | `students-service` |
| `aulaboh_attendance` | `attendance-service` |
| `aulaboh_grades` | `grades-service` |

Cada servicio conserva su propio esquema y no comparte tablas con otros modulos.

## Migraciones

Cada microservicio mantiene sus migraciones en `src/main/resources/db/migration`.

| Servicio | Migracion inicial |
|---|---|
| `students-service` | `V1__create_students_table.sql` |
| `attendance-service` | `V1__create_attendance_tables.sql` |
| `grades-service` | `V1__create_grades_tables.sql` |

Flyway crea la tabla `flyway_schema_history` en cada base y registra las migraciones aplicadas. Hibernate usa `ddl-auto=validate`, por lo que solo valida el esquema existente y no lo modifica automaticamente.

## Inicio del entorno

```powershell
cd platform/database
docker compose up -d
```

Luego iniciar los microservicios de dominio:

```powershell
cd services/students-service
mvn spring-boot:run
```

```powershell
cd services/attendance-service
mvn spring-boot:run
```

```powershell
cd services/grades-service
mvn spring-boot:run
```

## Verificacion de esquema

```powershell
docker exec -it aulaboh-postgres psql -U aulaboh -d aulaboh_students -c "\dt"
docker exec -it aulaboh-postgres psql -U aulaboh -d aulaboh_attendance -c "\dt"
docker exec -it aulaboh-postgres psql -U aulaboh -d aulaboh_grades -c "\dt"
```

## Validacion de persistencia

1. Levantar PostgreSQL y los servicios de dominio.
2. Crear un estudiante, una clase, una asistencia, una evaluacion y una calificacion.
3. Consultar los registros para confirmar su creacion.
4. Detener solo los servicios Java.
5. Volver a iniciar los servicios sin ejecutar `docker compose down -v`.
6. Consultar nuevamente los mismos registros.

Resultado esperado:

- los datos siguen disponibles despues del reinicio de los servicios;
- las tablas `flyway_schema_history` mantienen la migracion `V1` aplicada;
- Flyway informa que el esquema esta actualizado y no vuelve a ejecutar migraciones existentes.
