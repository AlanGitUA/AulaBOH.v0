# Archivos corregidos en versión final AulaBOH

## Problemas encontrados

1. Faltaba el `pom.xml` principal en la raíz del proyecto.
2. El BFF podía quedar con rutas duplicadas entre `StudentProxyController` y `AcademicSummaryController`.
3. Faltaban DTOs del BFF requeridos por los controladores y clientes.
4. La carpeta `apps/frontend/src` estaba incompleta en el ZIP recibido.
5. El frontend debía consumir el BFF en `/api/bff/...` para demostrar correctamente el patrón Facade.

## Correcciones realizadas

### Raíz del proyecto
- `pom.xml`: agregado como proyecto Maven multi-módulo.

### BFF
- Eliminado `StudentProxyController.java` para evitar conflicto de rutas.
- Agregados DTOs faltantes en `apps/bff/src/main/java/cl/aulaboh/bff/dto/`:
  - `StudentRequest.java`
  - `StudentResponse.java`
  - `ClassRequest.java`
  - `ClassResponse.java`
  - `AttendanceRequest.java`
  - `AttendanceResponse.java`
  - `AttendanceSummaryResponse.java`
  - `EvaluationRequest.java`
  - `EvaluationResponse.java`
  - `GradeRequest.java`
  - `GradeResponse.java`
  - `AcademicSummaryResponse.java`

### Frontend
- Agregados archivos completos en `apps/frontend/src/`:
  - `main.jsx`
  - `App.jsx`
  - `styles.css`
  - `pages/StudentsPage.jsx`
  - `components/StudentForm.jsx`
  - `components/AcademicForms.jsx`
  - `services/apiClient.js`
  - `services/studentApi.js`
  - `services/bffApi.js`

## Flujo esperado

1. Frontend React consume el BFF en `http://localhost:8080/api/bff`.
2. BFF orquesta llamadas hacia:
   - `students-service` en `8081`.
   - `attendance-service` en `8084`.
   - `grades-service` en `8083`.
3. El frontend permite registrar:
   - Estudiantes.
   - Clases.
   - Asistencias.
   - Evaluaciones.
   - Calificaciones.
4. El resumen académico se obtiene desde el BFF usando Facade.
