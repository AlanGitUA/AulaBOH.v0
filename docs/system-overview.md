# Vista general del sistema

AulaBOH esta organizado como una plataforma de microservicios con frontend React, BFF, servicios de dominio y componentes transversales de infraestructura.

## Componentes principales

- `apps/frontend`: aplicacion React para operacion del libro de clases digital.
- `apps/bff`: Backend For Frontend encargado de centralizar y orquestar el consumo de servicios.
- `services/students-service`: gestion de estudiantes.
- `services/attendance-service`: gestion de clases y asistencias.
- `services/grades-service`: gestion de evaluaciones y calificaciones.
- `platform/discovery-server`: registro y descubrimiento de servicios con Eureka.
- `platform/api-gateway`: entrada centralizada para las rutas backend.
- `platform/keycloak`: autenticacion, roles y administracion de sesiones.
- `platform/database`: instancia PostgreSQL para persistencia de servicios de dominio.

## Flujo operativo

1. El frontend consume el BFF mediante rutas `/api/bff/...`.
2. El BFF orquesta llamadas hacia:
   - `students-service` en `8081`.
   - `attendance-service` en `8084`.
   - `grades-service` en `8083`.
3. El API Gateway expone rutas centralizadas en `8090`.
4. Los servicios backend se registran en Eureka para monitoreo y descubrimiento.
5. Keycloak administra autenticacion, sesiones y roles.
6. Los microservicios de dominio persisten datos en PostgreSQL y versionan su esquema con Flyway.

## Funcionalidades disponibles

- Registro, consulta, actualizacion y eliminacion de estudiantes.
- Asociacion de estudiantes con usuarios autenticados y apoderados.
- Registro y consulta de clases.
- Registro y consulta de asistencia.
- Registro, actualizacion y consulta de evaluaciones.
- Registro y consulta de calificaciones.
- Consulta de resumen academico segun el rol autenticado.

## Persistencia

Cada servicio de dominio usa una base propia:

| Base | Servicio |
|---|---|
| `aulaboh_students` | `students-service` |
| `aulaboh_attendance` | `attendance-service` |
| `aulaboh_grades` | `grades-service` |

Flyway aplica las migraciones iniciales y Hibernate valida el esquema al iniciar.

## Organizacion tecnica

El proyecto usa un `pom.xml` raiz como build Maven multi-modulo. Cada servicio mantiene sus propios controladores, DTOs, repositorios y reglas de negocio, mientras que el BFF concentra las respuestas orientadas al frontend.
