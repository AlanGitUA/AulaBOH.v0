# Pruebas y calidad

La solucion incorpora pruebas unitarias y web para validar reglas de negocio, contratos HTTP, clientes internos, fallbacks, manejo de excepciones y observabilidad. La cobertura se genera con JaCoCo desde Maven y la meta vigente es mantener al menos 80% de cobertura por modulo principal.

## Componentes cubiertos

| Componente | Cobertura principal |
|---|---|
| Students Service | Servicio, controlador MVC, excepciones y aspecto de logs. |
| Attendance Service | Servicio, controlador MVC, cliente de estudiantes, fallbacks, excepciones y aspecto de logs. |
| Grades Service | Servicio, controlador MVC, cliente de estudiantes, fallbacks, excepciones y aspecto de logs. |
| BFF | Fachada academica, controladores MVC, clientes HTTP, fallbacks, excepciones y aspecto de logs. |

## Tipos de pruebas

- Reglas de negocio sobre creacion, consulta, actualizacion, validaciones y casos inexistentes.
- Controladores con `MockMvc` para validar respuestas HTTP y seguridad basica del contrato.
- Clientes internos y respuestas degradadas ante dependencias no disponibles.
- `ApiExceptionHandler` para errores de negocio, validacion y fallas inesperadas.
- `ServiceLoggingAspect` para verificar registro de tiempos y errores.

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

## Estado actual de cobertura

| Modulo | Cobertura |
|---|---:|
| `apps/bff` | `85.4%` |
| `services/students-service` | `87.9%` |
| `services/attendance-service` | `91.5%` |
| `services/grades-service` | `93.4%` |

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
- Cada modulo principal mantiene al menos 80% de cobertura.
- Las reglas de negocio principales quedan cubiertas por pruebas unitarias.
- Los endpoints principales quedan cubiertos por pruebas web.
- El BFF queda cubierto como capa de orquestacion.
- Los fallbacks, handlers y logs criticos tienen validacion automatizada.
- Los reportes de cobertura pueden generarse con `mvn verify`.
