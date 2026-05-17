# Logs de operación desde el frontend

El sistema registra operaciones HTTP originadas desde el frontend y procesadas por el BFF y los microservicios.

## Flujo de verificación

1. Levantar Eureka.
2. Levantar `students-service`, `attendance-service`, `grades-service` y `bff`.
3. Levantar el frontend.
4. Abrir la consola del BFF y del microservicio que se va a probar.
5. Usar una acción del frontend, por ejemplo listar estudiantes o registrar asistencia.
6. Verificar que en consola aparece un log con:
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

## Uso operativo

Al usar el frontend, cada acción genera una solicitud hacia el BFF y luego hacia los microservicios. Cada solicitud queda registrada con su tiempo de respuesta, estado HTTP y resultado. Esto permite observar el comportamiento real del sistema y detectar operaciones lentas o con errores.

Actuator queda disponible para métricas internas, mientras que estos logs permiten una lectura directa de las operaciones HTTP procesadas.
