# Students Service

Microservicio de gestión de estudiantes del Colegio Bernardo O'Higgins.

## Patrones demostrados

- **Repository Pattern:** `repository/StudentRepository.java` separa acceso a datos de la lógica de negocio.
- **Factory Method:** `factory/StudentFactory.java` centraliza la creación de estudiantes activos.
- **Arquitectura en capas:** `controller`, `service`, `repository`, `model`, `dto`.

## Ejecutar

```bash
mvn spring-boot:run
```

Puerto: `8081`

## Endpoints principales

- `POST /api/students`
- `GET /api/students`
- `GET /api/students/{id}`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`

## Documentacion API

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
