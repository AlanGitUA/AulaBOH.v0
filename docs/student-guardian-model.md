# Modelo estudiante-apoderado

## Estado actual

`students-service` persiste dos campos de asociacion en la tabla `students`:

- `student_username`: usuario de Keycloak asociado al estudiante.
- `guardian_username`: usuario de Keycloak asociado al apoderado responsable.

El contrato REST expone esos valores como:

- `studentUsername`
- `guardianUsername`

Este modelo permite:

- obtener el resumen academico propio de un estudiante autenticado;
- listar los estudiantes asociados a un apoderado;
- permitir que un apoderado consulte el resumen academico de sus estudiantes asociados;
- impedir que un apoderado consulte estudiantes fuera de su alcance.

## Criterio de diseno

La asociacion se mantiene en `students-service` porque ese servicio es el propietario del agregado `Student` y resuelve la relacion entre identidad autenticada y estudiante del dominio.

La solucion actual cubre el caso de un estudiante con un apoderado responsable y varios estudiantes asociados al mismo apoderado, sin introducir una tabla intermedia innecesaria para el alcance vigente.

## Evolucion posible

Si el dominio requiere multiples apoderados por estudiante o metadatos de relacion, la evolucion natural es separar la asociacion en una tabla dedicada:

```text
students
- id
- first_name
- last_name
- course
- email
- student_username

guardian_student_relations
- id
- guardian_username
- student_id
- relationship_type
- active
```

## Criterio para cambiar el modelo

El modelo actual puede mantenerse mientras se cumplan estas condiciones:

- un estudiante solo necesita un apoderado principal;
- no se requiere registrar parentesco, prioridad o vigencia;
- el BFF puede resolver autorizacion con `studentUsername` y `guardianUsername` sin ampliar el contrato publico.

Si cualquiera de esas condiciones cambia, conviene migrar a una relacion dedicada y conservar el contrato externo mediante adaptacion interna.
