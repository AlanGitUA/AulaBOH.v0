# Resiliencia y Circuit Breaker

La plataforma utiliza Resilience4j para aislar fallas entre componentes y mantener respuestas controladas cuando un servicio dependiente no esta disponible.

## Componentes protegidos

| Componente | Cliente protegido | Circuit breaker | Comportamiento de fallback |
|---|---|---|---|
| BFF | `StudentClient` | `studentsService` | Listados vacios para consultas generales. Error `503` para operaciones criticas. |
| BFF | `AttendanceClient` | `attendanceService` | Listados vacios o resumen con totales en cero. Error `503` para escrituras. |
| BFF | `GradesClient` | `gradesService` | Listados vacios para consultas. Error `503` para escrituras. |
| Attendance Service | `StudentClient` | `studentsService` | Error `503` si no es posible validar el estudiante. |
| Grades Service | `StudentClient` | `studentsService` | Error `503` si no es posible validar el estudiante. |

## Configuracion base

Las instancias usan una ventana reducida para facilitar pruebas locales:

```properties
resilience4j.circuitbreaker.instances.studentsService.sliding-window-size=5
resilience4j.circuitbreaker.instances.studentsService.minimum-number-of-calls=3
resilience4j.circuitbreaker.instances.studentsService.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.studentsService.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.studentsService.permitted-number-of-calls-in-half-open-state=2
```

En el BFF tambien existen instancias para `attendanceService` y `gradesService`.

## Criterio de diseno

Las consultas pueden degradar parcialmente cuando una dependencia falla. Por ejemplo, el BFF puede devolver un resumen con asistencia en cero o calificaciones vacias si el servicio correspondiente no responde.

Las operaciones de escritura no usan datos inventados. Si un servicio requerido no esta disponible, la operacion falla con `503 Service Unavailable`.

## Prueba manual recomendada

### 1. Compilar

```bash
mvn clean compile
```

### 2. Levantar servicios

Abrir una terminal por componente:

```bash
cd services/students-service
mvn spring-boot:run
```

```bash
cd services/attendance-service
mvn spring-boot:run
```

```bash
cd services/grades-service
mvn spring-boot:run
```

```bash
cd apps/bff
mvn spring-boot:run
```

### 3. Validar funcionamiento normal

```bash
curl http://localhost:8080/api/bff/students
curl http://localhost:8080/api/bff/classes
curl http://localhost:8080/api/bff/evaluations
```

### 4. Probar degradacion del BFF

Detener `attendance-service` y ejecutar:

```bash
curl http://localhost:8080/api/bff/classes
```

Resultado esperado: respuesta `200` con lista vacia.

Detener `grades-service` y ejecutar:

```bash
curl http://localhost:8080/api/bff/evaluations
```

Resultado esperado: respuesta `200` con lista vacia.

### 5. Probar escritura protegida

Con `attendance-service` apagado:

```bash
curl -X POST http://localhost:8080/api/bff/classes \
  -H "Content-Type: application/json" \
  -d "{\"course\":\"1A\",\"subject\":\"Matematica\",\"classDate\":\"2026-05-13\"}"
```

Resultado esperado: respuesta `503` con error `DownstreamServiceUnavailableException`.

### 6. Probar validacion de estudiantes

Levantar `attendance-service` o `grades-service` y detener `students-service`.

Intentar registrar asistencia o calificacion. Resultado esperado:

- `attendance-service` responde `503` si no puede validar el estudiante.
- `grades-service` responde `503` si no puede validar el estudiante.
- No se registra informacion cuando la validacion de estudiante no puede realizarse.

## Criterios de aceptacion

- El proyecto compila con `mvn clean compile`.
- El BFF no retorna error `500` ante fallas de lectura degradables.
- Las escrituras fallan con `503` cuando una dependencia obligatoria no esta disponible.
- Attendance y Grades no registran datos si no pueden validar el estudiante.
- Los nombres de circuit breaker coinciden con la configuracion en `application.properties`.
