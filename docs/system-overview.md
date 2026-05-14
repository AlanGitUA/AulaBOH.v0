# Vista general del sistema

AulaBOH está organizado como una plataforma de microservicios con frontend React, BFF, servicios de dominio y componentes transversales de infraestructura.

## Componentes principales

- `apps/frontend`: aplicación React para operación del libro de clases digital.
- `apps/bff`: Backend For Frontend encargado de centralizar y orquestar el consumo de servicios.
- `services/students-service`: gestión de estudiantes.
- `services/attendance-service`: gestión de clases y asistencias.
- `services/grades-service`: gestión de evaluaciones y calificaciones.
- `platform/discovery-server`: registro y descubrimiento de servicios con Eureka.
- `platform/api-gateway`: entrada centralizada para las rutas backend.
- `platform/keycloak`: autenticación, roles y administración de sesiones.

## Flujo operativo

1. El frontend consume el BFF mediante rutas `/api/bff/...`.
2. El BFF orquesta llamadas hacia:
   - `students-service` en `8081`.
   - `attendance-service` en `8084`.
   - `grades-service` en `8083`.
3. El API Gateway expone rutas centralizadas en `8090`.
4. Los servicios backend se registran en Eureka para monitoreo y descubrimiento.
5. Keycloak administra la base de seguridad mediante realm, clientes, roles y usuarios iniciales.

## Funcionalidades disponibles

- Registro y consulta de estudiantes.
- Registro de clases.
- Registro de asistencia.
- Registro de evaluaciones.
- Registro de calificaciones.
- Consulta de resumen de estudiante desde el BFF.

## Organización técnica

El proyecto usa un `pom.xml` raíz como build Maven multi-módulo. Cada servicio mantiene sus propios controladores, DTOs, repositorios y reglas de negocio, mientras que el BFF concentra las respuestas orientadas al frontend.
