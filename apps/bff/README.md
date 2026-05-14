# BFF - Backend For Frontend

Capa de integración entre el frontend y los microservicios.

## Patrones demostrados

- **Facade Pattern:** `AcademicSummaryFacade` reúne información de estudiantes, asistencia y calificaciones.
- **Client/Adapter:** clases `StudentClient`, `AttendanceClient` y `GradesClient` encapsulan llamadas HTTP.
- **BFF:** expone endpoints orientados al frontend, evitando que React consuma cada microservicio por separado.

## Ejecutar

```bash
mvn spring-boot:run
```

Puerto: `8080`

## Endpoints

- `GET /api/bff/students`
- `POST /api/bff/students`
- `GET /api/bff/students/{studentId}/summary`
- `POST /api/bff/classes`
- `GET /api/bff/classes`
- `POST /api/bff/attendances`
- `GET /api/bff/attendances/student/{studentId}`
- `POST /api/bff/evaluations`
- `GET /api/bff/evaluations`
- `POST /api/bff/grades`

## Documentacion API

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Resiliencia

El BFF aplica circuit breakers para `studentsService`, `attendanceService` y `gradesService`. Las consultas pueden devolver respuestas degradadas, como listas vacias, mientras que las escrituras retornan `503` si el servicio requerido no esta disponible.

Guia completa: `docs/resilience-circuit-breaker.md`
