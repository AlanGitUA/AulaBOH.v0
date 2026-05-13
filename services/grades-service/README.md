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
- `POST /api/grades`
- `GET /api/grades/student/{studentId}`

## Documentacion API

- Swagger UI: `http://localhost:8083/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`
