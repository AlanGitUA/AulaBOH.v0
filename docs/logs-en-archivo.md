# Logs locales en archivo

El proyecto guarda logs en archivos locales por cada microservicio.

## Archivos generados

Cada servicio crea una carpeta `logs` dentro de su propia carpeta de ejecución.

Ejemplo en `students-service`:

```txt
services/students-service/logs/application.log
services/students-service/logs/operaciones.log
```

## Diferencia entre archivos

### `application.log`

Contiene logs generales del microservicio:

- inicio del servicio
- errores internos
- logs de Eureka
- Hibernate
- ejecución de métodos internos
- excepciones

### `operaciones.log`

Contiene solo logs limpios de operaciones HTTP, pensados para mostrar en la presentación:

- servicio que recibió la operación
- método HTTP
- endpoint utilizado
- estado HTTP
- tiempo de respuesta
- resultado

Ejemplo:

```txt
============================================================
OPERACION HTTP FINALIZADA
Servicio         : students-service
Metodo HTTP      : POST
Endpoint         : /api/students
Estado HTTP      : 201
Tiempo respuesta : 4 ms
Resultado        : OK
============================================================
```

## Ver logs en tiempo real

### BFF

```powershell
cd C:\proyectos\AulaBOH.v0\apps\bff
Get-Content .\logs\operaciones.log -Wait -Tail 50
```

### Students service

```powershell
cd C:\proyectos\AulaBOH.v0\services\students-service
Get-Content .\logs\operaciones.log -Wait -Tail 50
```

### Attendance service

```powershell
cd C:\proyectos\AulaBOH.v0\services\attendance-service
Get-Content .\logs\operaciones.log -Wait -Tail 50
```

### Grades service

```powershell
cd C:\proyectos\AulaBOH.v0\services\grades-service
Get-Content .\logs\operaciones.log -Wait -Tail 50
```

## Explicación para el profesor

Además de mostrar los logs en consola, se configuró Logback para guardar los registros localmente en archivos. Se separaron los logs generales del sistema en `application.log` y los logs de operaciones HTTP en `operaciones.log`. Esto permite revisar de forma ordenada el comportamiento de cada microservicio, incluyendo endpoint utilizado, estado de respuesta y tiempo de respuesta.
