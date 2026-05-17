# Observabilidad, métricas, logs y Circuit Breaker - AulaBOH

## Objetivo
Se incorporó observabilidad en los microservicios para registrar comportamiento, tiempos de respuesta, errores y activación de Circuit Breaker. Esto permite evaluar el rendimiento de cada microservicio y detectar fallas de comunicación o errores internos.

## Componentes implementados

### 1. Logs de tiempos de respuesta
Se agregó `ServiceLoggingAspect` usando AOP en:

- `students-service`
- `attendance-service`
- `grades-service`
- `bff`

Este aspecto registra:

- Inicio del método.
- Fin del método.
- Tiempo de respuesta en milisegundos.
- Error ocurrido y tiempo antes del error.

Ejemplo de log esperado:

```txt
INFO  Iniciando metodo: StudentService.findAll
INFO  Metodo finalizado: StudentService.findAll | Tiempo de respuesta: 23 ms
```

### 2. Circuit Breaker en métodos críticos
Se agregó `@CircuitBreaker` en los métodos públicos de:

- `StudentService`
- `AttendanceService`
- `GradeService`
- `AcademicSummaryFacade`

Además, se mantienen los Circuit Breaker existentes en los clientes que comunican microservicios:

- BFF hacia `students-service`
- BFF hacia `attendance-service`
- BFF hacia `grades-service`
- `attendance-service` hacia `students-service`
- `grades-service` hacia `students-service`

### 3. Logs en fallback
Los métodos fallback ahora registran cuándo se activa el Circuit Breaker, indicando:

- Servicio afectado.
- Método afectado.
- Parámetros principales, cuando corresponde.
- Error original.

### 4. Handler Exception global con logs
Los `ApiExceptionHandler` ahora registran errores de negocio, validación, servicios no disponibles, Circuit Breaker abierto y errores inesperados.

### 5. Métricas con Actuator
Se habilitaron endpoints:

```txt
/actuator/health
/actuator/info
/actuator/metrics
/actuator/circuitbreakers
```

## Cómo probar

1. Levantar Eureka.
2. Levantar los microservicios.
3. Levantar BFF y API Gateway.
4. Llamar endpoints normalmente y revisar consola/logs.
5. Apagar un microservicio, por ejemplo `students-service`.
6. Consultar desde el BFF un endpoint que dependa de estudiantes.
7. Verificar logs de fallback y respuesta controlada.

## Comportamiento esperado

Se implementó observabilidad transversal con AOP para medir tiempos de respuesta en los métodos críticos de los servicios. También se aplicó Circuit Breaker para evitar que fallas repetidas afecten todo el sistema. Cuando un microservicio no responde o el Circuit Breaker se abre, el sistema registra el evento y entrega una respuesta controlada mediante el handler global de excepciones.
