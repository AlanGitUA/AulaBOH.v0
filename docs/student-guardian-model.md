# Modelo estudiante-apoderado

## Estado actual

El servicio de estudiantes mantiene dos campos de asociacion:

- `studentUsername`: usuario de Keycloak asociado al estudiante.
- `guardianUsername`: usuario de Keycloak asociado al apoderado responsable.

Este modelo permite:

- obtener el resumen propio de un estudiante autenticado;
- listar los estudiantes asociados a un apoderado;
- impedir que un apoderado consulte estudiantes fuera de su alcance.

## Motivo de la decision

La aplicacion necesita resolver permisos antes de introducir persistencia definitiva. Usar los nombres de usuario de Keycloak permite mantener el flujo funcional con cambios pequenos y deja clara la relacion que la base de datos debera conservar.

## Evolucion recomendada con base de datos

Cuando se implemente persistencia relacional, conviene separar la relacion apoderado-estudiante si el dominio requiere:

- mas de un apoderado por estudiante;
- mas de un estudiante por apoderado;
- metadatos de relacion, como parentesco, prioridad o vigencia.

Modelo recomendado:

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

## Criterio para la siguiente iteracion

Antes de pasar a una tabla de relacion dedicada, confirmar si el alcance funcional requiere multiples apoderados por estudiante. Si no se requiere todavia, el modelo actual puede mantenerse durante la primera integracion con base de datos y migrarse posteriormente sin cambiar el contrato del BFF.
