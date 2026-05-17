# Grades Service

Microservicio de evaluaciones y calificaciones.

## Patrones demostrados

- **Repository Pattern:** repositorios JPA para evaluaciones y calificaciones.
- **Adapter/Client Pattern:** `StudentClient` valida estudiantes contra `students-service`.
- **Arquitectura en capas:** controller, service, repository, model y dto.

## Ejecutar

```bash
mvn spring-boot:run
```

Puerto: `8083`

## Endpoints principales

- `POST /api/evaluations`
- `GET /api/evaluations`
- `PUT /api/evaluations/{id}`
- `POST /api/grades`
- `GET /api/grades/student/{studentId}`

## Documentacion API

- Swagger UI: `http://localhost:8083/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`

## Resiliencia

La validacion contra `students-service` esta protegida con el circuit breaker `studentsService`. Si el servicio de estudiantes no esta disponible, el registro de calificaciones responde `503` y no persiste datos.
