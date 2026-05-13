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
