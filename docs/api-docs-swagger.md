# Documentacion OpenAPI y Swagger

La plataforma expone documentacion interactiva para el BFF y los microservicios principales mediante Springdoc OpenAPI.

## Componentes documentados

| Componente | Puerto | Swagger UI | OpenAPI JSON |
|---|---:|---|---|
| BFF | `8080` | `http://localhost:8080/swagger-ui/index.html` | `http://localhost:8080/v3/api-docs` |
| Students Service | `8081` | `http://localhost:8081/swagger-ui/index.html` | `http://localhost:8081/v3/api-docs` |
| Grades Service | `8083` | `http://localhost:8083/swagger-ui/index.html` | `http://localhost:8083/v3/api-docs` |
| Attendance Service | `8084` | `http://localhost:8084/swagger-ui/index.html` | `http://localhost:8084/v3/api-docs` |

## Alcance implementado

- Dependencia `springdoc-openapi-starter-webmvc-ui` configurada en BFF y microservicios REST.
- Metadata OpenAPI por componente: titulo, descripcion, version, contacto y servidor local.
- Controladores anotados con `@Tag`, `@Operation` y `@ApiResponse`.
- Endpoints organizados por dominio funcional para facilitar pruebas y revision tecnica.

## Orden recomendado de revision

1. Ejecutar `students-service`.
2. Ejecutar `attendance-service`.
3. Ejecutar `grades-service`.
4. Ejecutar `bff`.
5. Abrir cada Swagger UI y validar que los endpoints se agrupen correctamente.

## Criterios de aceptacion

- Cada componente expone `/swagger-ui/index.html`.
- Cada componente expone `/v3/api-docs`.
- Las operaciones principales muestran resumen y descripcion.
- Las respuestas esperadas incluyen codigos HTTP relevantes.
- La documentacion mantiene nombres y responsabilidades consistentes con la arquitectura.
