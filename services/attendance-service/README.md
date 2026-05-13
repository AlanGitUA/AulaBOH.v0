# Attendance Service

Microservicio de clases y asistencia.

## Patrones demostrados

- **Repository Pattern:** `AttendanceRepository` y `SchoolClassRepository` aíslan el acceso a datos.
- **Adapter/Client Pattern:** `client/StudentClient.java` encapsula la comunicación con `students-service`.
- **Arquitectura en capas:** `controller`, `service`, `repository`, `model`, `dto`.

## Ejecutar

Primero ejecutar `students-service`, luego:

```bash
mvn spring-boot:run
```

Puerto: `8084`

## Endpoints principales

- `POST /api/classes`
- `GET /api/classes`
- `POST /api/attendances`
- `GET /api/attendances/student/{studentId}`
- `GET /api/attendances/student/{studentId}/summary`

## Documentacion API

- Swagger UI: `http://localhost:8084/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8084/v3/api-docs`
