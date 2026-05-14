# Pruebas y calidad

La solucion incorpora pruebas unitarias para validar reglas de negocio y orquestacion entre componentes. La cobertura se genera con JaCoCo desde Maven.

## Componentes cubiertos

| Componente | Archivo de prueba | Alcance |
|---|---|---|
| Students Service | `StudentServiceTest` | Creacion, busqueda, filtro por curso, actualizacion y manejo de estudiantes inexistentes. |
| Attendance Service | `AttendanceServiceTest` | Creacion de clases, registro de asistencia, bloqueo de duplicados y resumen por estado. |
| Grades Service | `GradeServiceTest` | Creacion de evaluaciones, registro de calificaciones, rango valido de notas y evaluaciones inexistentes. |
| BFF | `AcademicSummaryFacadeTest` | Orquestacion de estudiantes, asistencia y calificaciones para respuestas orientadas al frontend. |

## Ejecutar pruebas

Desde la raiz del repositorio:

```bash
mvn test
```

Para limpiar y ejecutar toda la suite:

```bash
mvn clean test
```

## Generar reportes de cobertura

```bash
mvn verify
```

JaCoCo genera reportes por modulo:

```text
services/students-service/target/site/jacoco/index.html
services/attendance-service/target/site/jacoco/index.html
services/grades-service/target/site/jacoco/index.html
apps/bff/target/site/jacoco/index.html
```

## Validacion esperada

La ejecucion de pruebas debe finalizar con:

```text
BUILD SUCCESS
```

Los reportes Surefire deben mostrar:

```text
Failures: 0
Errors: 0
```

## Criterios de aceptacion

- La suite de pruebas se ejecuta desde Maven sin dependencias manuales adicionales.
- Las reglas de negocio principales quedan cubiertas por pruebas unitarias.
- El BFF queda cubierto como capa de orquestacion.
- Los reportes de cobertura pueden generarse con `mvn verify`.
- El proyecto mantiene una base de calidad verificable antes de integrar cambios en `develop`.
