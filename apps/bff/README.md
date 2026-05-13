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
- `GET /api/bff/students/{studentId}/summary`
