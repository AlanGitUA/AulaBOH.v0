# AulaBOH - Colegio Bernardo O'Higgins

Plataforma de Libro de Clases Digital basada en microservicios.

## Estructura

```text
apps/bff                         Backend For Frontend
apps/frontend                    Frontend React
services/students-service        Microservicio de estudiantes
services/attendance-service      Microservicio de asistencia
services/grades-service          Microservicio de calificaciones
platform/api-gateway             API Gateway
platform/discovery-server        Eureka Server
packages/frontend-components     Componentes NPM reutilizables
archetypes/                      Arquetipos Maven
```

## Patrones demostrados

| Patrón | Evidencia en código | Uso |
|---|---|---|
| Repository Pattern | `repository/` en microservicios | Separar persistencia de lógica de negocio |
| Factory Method | `StudentFactory` | Crear estudiantes con valores por defecto |
| Facade | `AcademicSummaryFacade` en BFF | Simplificar llamadas del frontend |
| Adapter/Client | `StudentClient`, `AttendanceClient`, `GradesClient` | Encapsular comunicación HTTP |
| Module Pattern | `components`, `pages`, `services` en React | Ordenar frontend por responsabilidades |
| Microservicios | `services/` | Separar módulos del sistema |
| BFF | `apps/bff` | Adaptar backend a necesidades del frontend |

## Ejecución recomendada

Primero compilar desde la raíz del proyecto:

```bash
mvn clean install -DskipTests
```

Luego abrir una terminal por componente:

```bash
cd services/students-service
mvn spring-boot:run
```

```bash
cd services/attendance-service
mvn spring-boot:run
```

```bash
cd services/grades-service
mvn spring-boot:run
```

```bash
cd apps/bff
mvn spring-boot:run
```

```bash
cd apps/frontend
npm install
npm run dev
```

## Puertos

- BFF: `8080`
- Students Service: `8081`
- Attendance Service: `8084`
- Grades Service: `8083`
- API Gateway: `8090`
- Discovery Server: `8761`
- Frontend: `5173`

## Pruebas y calidad

El backend incluye pruebas unitarias para los microservicios principales y el BFF. La cobertura se genera con JaCoCo.

```bash
mvn test
mvn verify
```

Guia completa: `docs/testing-quality.md`


## Flujo operativo desde frontend

1. Registrar estudiante.
2. Registrar clase.
3. Registrar asistencia.
4. Registrar evaluación.
5. Registrar calificación.
6. Presionar **Ver resumen** para validar asistencia y calificaciones.

Todo el frontend consume el BFF mediante `/api/bff/...`, manteniendo una capa centralizada de orquestación hacia los microservicios.
