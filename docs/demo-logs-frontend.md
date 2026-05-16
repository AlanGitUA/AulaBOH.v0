# Demo de logs desde el frontend

Este proyecto tiene logs de operación HTTP para que la demostración sea más clara que mostrar directamente `/actuator/metrics`.

## Flujo recomendado para la presentación

1. Levantar Eureka.
2. Levantar `students-service`, `attendance-service`, `grades-service` y `bff`.
3. Levantar el frontend.
4. Abrir la consola del BFF y del microservicio que se va a probar.
5. Usar una acción del frontend, por ejemplo listar estudiantes o registrar asistencia.
6. Mostrar que en consola aparece un log con:
   - servicio que recibió la solicitud;
   - método HTTP;
   - endpoint;
   - estado HTTP;
   - tiempo de respuesta;
   - resultado de la operación.

## Ejemplo de log esperado

```txt
============================================================
OPERACION HTTP FINALIZADA
Servicio         : bff
Metodo HTTP      : GET
Endpoint         : /api/students
Estado HTTP      : 200
Tiempo respuesta : 87 ms
Resultado        : OK
============================================================
```

Y luego en el microservicio correspondiente:

```txt
============================================================
OPERACION HTTP FINALIZADA
Servicio         : students-service
Metodo HTTP      : GET
Endpoint         : /api/students
Estado HTTP      : 200
Tiempo respuesta : 31 ms
Resultado        : OK
============================================================
```

## Explicación breve para el profesor

Al usar el frontend, cada acción genera una solicitud hacia el BFF y luego hacia los microservicios. Cada solicitud queda registrada con su tiempo de respuesta, estado HTTP y resultado. Esto permite observar el comportamiento real del sistema y detectar operaciones lentas o con errores.

Actuator sigue quedando disponible como respaldo técnico para métricas internas, pero para la demo se recomienda mostrar estos logs porque son más claros y fáciles de interpretar.
