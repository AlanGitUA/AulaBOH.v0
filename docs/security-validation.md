# Validacion de seguridad por roles

## Objetivo

Verificar que el BFF exija autenticacion JWT y aplique autorizacion segun el rol entregado por Keycloak.

## Cobertura automatizada

El modulo `apps/bff` incluye pruebas WebMvc para validar:

- `401 Unauthorized` cuando no se envia token.
- `403 Forbidden` cuando un estudiante intenta consultar el listado general.
- acceso permitido para docentes en consultas generales.
- bloqueo de creacion de estudiantes para docentes.
- acceso al resumen propio de un estudiante.
- acceso de un apoderado a un estudiante asociado.

Comando:

```bash
mvn -pl apps/bff -am test
```

## Validacion manual

Usuarios iniciales:

| Usuario | Rol |
|---|---|
| `admin.aulaboh` | `ADMIN` |
| `docente.demo` | `DOCENTE` |
| `estudiante.demo` | `ESTUDIANTE` |
| `apoderado.demo` | `APODERADO` |

Flujo recomendado:

1. Iniciar Keycloak, Eureka, microservicios, BFF, Gateway y frontend.
2. Crear un estudiante asociado a:
   - `studentUsername=estudiante.demo`
   - `guardianUsername=apoderado.demo`
3. Registrar asistencia y calificaciones para ese estudiante.
4. Validar accesos por rol:
   - `ADMIN`: panel, estudiantes, gestion academica y resumen general.
   - `DOCENTE`: panel, gestion academica y resumen general.
   - `ESTUDIANTE`: solo su propio resumen.
   - `APODERADO`: solo estudiantes asociados a su cuenta.
5. Verificar que el Gateway exponga solo `/api/bff/**`.

## Resultado esperado

- Las rutas del BFF no publicas rechazan solicitudes sin token.
- Las operaciones administrativas quedan restringidas a sus roles autorizados.
- Los usuarios de alcance individual no pueden consultar informacion de otros estudiantes.
