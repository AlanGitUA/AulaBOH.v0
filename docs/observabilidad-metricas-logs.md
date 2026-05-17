# Observabilidad, metricas, logs y Circuit Breaker - AulaBOH

## Objetivo

La plataforma incorpora observabilidad en los microservicios para registrar comportamiento, tiempos de respuesta, errores y activacion de circuit breakers. Esto permite evaluar rendimiento y detectar fallas de comunicacion o errores internos.

## Componentes implementados

### 1. Logs de tiempos de respuesta

Se agrego `ServiceLoggingAspect` usando AOP en:

- `students-service`
- `attendance-service`
- `grades-service`
- `bff`

Este aspecto registra:

- inicio del metodo;
- fin del metodo;
- tiempo de respuesta en milisegundos;
- error ocurrido y tiempo antes del error.

Ejemplo de log esperado:

```txt
INFO  Iniciando metodo: StudentService.findAll
INFO  Metodo finalizado: StudentService.findAll | Tiempo de respuesta: 23 ms
```

### 2. Circuit Breaker en metodos criticos

Se agrego `@CircuitBreaker` en los metodos publicos de:

- `StudentService`
- `AttendanceService`
- `GradeService`
- `AcademicSummaryFacade`

Ademas, se mantienen circuit breakers en los clientes que comunican microservicios:

- BFF hacia `students-service`
- BFF hacia `attendance-service`
- BFF hacia `grades-service`
- `attendance-service` hacia `students-service`
- `grades-service` hacia `students-service`

### 3. Logs en fallback

Los metodos fallback registran:

- servicio afectado;
- metodo afectado;
- parametros principales, cuando corresponde;
- error original.

### 4. Handler global de excepciones

Los `ApiExceptionHandler` registran errores de negocio, validacion, servicios no disponibles, circuit breaker abierto y errores inesperados.

### 5. Metricas con Actuator

Se habilitaron endpoints:

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/circuitbreakers
```

Ademas de las metricas base de Spring Boot, la plataforma expone metricas personalizadas:

| Metrica | Tipo | Uso |
|---|---|---|
| `aulaboh.method.execution` | Timer | Tiempo de ejecucion de metodos criticos. |
| `aulaboh.api.errors` | Counter | Errores HTTP manejados por cada servicio. |

Etiquetas disponibles para `aulaboh.method.execution`:

- `service`
- `class`
- `method`
- `outcome`

Etiquetas disponibles para `aulaboh.api.errors`:

- `service`
- `type`
- `status`

## Como probar

1. Levantar Eureka.
2. Levantar los microservicios.
3. Levantar BFF y API Gateway.
4. Llamar endpoints normalmente y revisar consola o logs.
5. Apagar un microservicio, por ejemplo `students-service`.
6. Consultar desde el BFF un endpoint que dependa de estudiantes.
7. Verificar logs de fallback y respuesta controlada.

## Consultar metricas

### Tiempo de ejecucion por metodo

Ejemplo general:

```text
http://localhost:8081/actuator/metrics/aulaboh.method.execution
```

Ejemplo filtrado por metodo:

```text
http://localhost:8081/actuator/metrics/aulaboh.method.execution?tag=service:students-service&tag=class:StudentService&tag=method:findAll&tag=outcome:success
```

### Errores manejados por la API

Ejemplo general:

```text
http://localhost:8081/actuator/metrics/aulaboh.api.errors
```

Ejemplo filtrado por validaciones fallidas:

```text
http://localhost:8081/actuator/metrics/aulaboh.api.errors?tag=service:students-service&tag=type:ValidationException&tag=status:400
```

## Prueba manual recomendada

1. Levantar `students-service`.
2. Ejecutar una consulta valida a `GET /api/students`.
3. Consultar `aulaboh.method.execution` y verificar que aparezca una serie con `outcome=success`.
4. Enviar un `POST /api/students` invalido, por ejemplo sin `firstName`.
5. Consultar `aulaboh.api.errors` y verificar que aparezca una serie con `type=ValidationException` y `status=400`.
6. Revisar el archivo de operaciones del servicio para confirmar que el evento tambien quedo registrado en logs.

## Comportamiento esperado

Se implemento observabilidad transversal con AOP para medir tiempos de respuesta en metodos criticos. Tambien se aplico Circuit Breaker para evitar que fallas repetidas afecten todo el sistema. Cuando un microservicio no responde o el circuito se abre, el sistema registra el evento, incrementa metricas consultables y entrega una respuesta controlada mediante el handler global de excepciones.
