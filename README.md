# AulaBOH - Colegio Bernardo O'Higgins

Plataforma de Libro de Clases Digital basada en microservicios, con autenticacion centralizada, frontend web y servicios de dominio desacoplados.

## Estructura

```text
apps/bff                         Backend For Frontend
apps/frontend                    Frontend React
services/students-service        Microservicio de estudiantes
services/attendance-service      Microservicio de asistencia
services/grades-service          Microservicio de calificaciones
platform/api-gateway             API Gateway
platform/discovery-server        Eureka Server
platform/keycloak                Autenticacion y roles
packages/frontend-components     Componentes NPM reutilizables
archetypes/                      Arquetipos Maven
```

## Arquitectura

| Componente | Responsabilidad |
|---|---|
| Frontend React | Interfaz operativa para los usuarios del sistema |
| BFF | Orquestacion de respuestas orientadas al frontend |
| API Gateway | Entrada publica del backend mediante `/api/bff/**` |
| Eureka Server | Registro y descubrimiento de servicios |
| Keycloak | Autenticacion, emision de tokens y gestion de roles |
| Microservicios | Gestion independiente de estudiantes, asistencia y calificaciones |

## Patrones aplicados

| Patron | Evidencia en codigo | Uso |
|---|---|---|
| Repository Pattern | `repository/` en microservicios | Separar persistencia de logica de negocio |
| Factory Method | `StudentFactory` | Crear estudiantes con valores por defecto |
| Facade | `AcademicSummaryFacade` en BFF | Simplificar llamadas del frontend |
| Adapter/Client | `StudentClient`, `AttendanceClient`, `GradesClient` | Encapsular comunicacion HTTP |
| Module Pattern | `components`, `pages`, `services` en React | Ordenar frontend por responsabilidades |
| Microservicios | `services/` | Separar modulos del sistema |
| BFF | `apps/bff` | Adaptar backend a necesidades del frontend |

## Ejecucion recomendada

Primero compilar desde la raiz del proyecto:

```bash
mvn clean install -DskipTests
```

Luego iniciar cada componente en una terminal separada, en este orden:

1. Keycloak
2. Discovery Server
3. Students Service
4. Attendance Service
5. Grades Service
6. BFF
7. API Gateway
8. Frontend

```powershell
cd platform/keycloak
docker compose up -d
```

```powershell
cd platform/discovery-server
mvn spring-boot:run
```

```powershell
cd services/students-service
mvn spring-boot:run
```

```powershell
cd services/attendance-service
mvn spring-boot:run
```

```powershell
cd services/grades-service
mvn spring-boot:run
```

```powershell
cd apps/bff
mvn spring-boot:run
```

```powershell
cd platform/api-gateway
mvn spring-boot:run
```

```powershell
cd apps/frontend
npm install
npm run dev
```

## Puertos

| Componente | Puerto |
|---|---:|
| BFF | `8080` |
| Students Service | `8081` |
| Grades Service | `8083` |
| Attendance Service | `8084` |
| Keycloak | `8089` |
| API Gateway | `8090` |
| Discovery Server | `8761` |
| Frontend | `5173` |

## Seguridad y acceso

Keycloak administra la autenticacion y los roles del sistema. El API Gateway expone unicamente las rutas del BFF mediante:

```text
http://localhost:8090/api/bff/**
```

Accesos principales:

| Rol | Alcance |
|---|---|
| `ADMIN` | Administracion general, estudiantes, gestion academica y resumen general |
| `DOCENTE` | Gestion academica y resumen general |
| `ESTUDIANTE` | Consulta de su propio resumen academico |
| `APODERADO` | Consulta de estudiantes asociados a su cuenta |

## Flujo operativo

1. Registrar estudiante.
2. Registrar clase.
3. Registrar asistencia.
4. Registrar evaluacion.
5. Registrar calificacion.
6. Consultar resumen academico segun el rol autenticado.

Todo el frontend consume el BFF mediante `/api/bff/...`, manteniendo una capa centralizada de orquestacion hacia los microservicios.

## Pruebas y calidad

El backend incluye pruebas unitarias y de seguridad para los microservicios principales y el BFF. La cobertura se genera con JaCoCo.

```bash
mvn test
mvn verify
```

## Documentacion

- `docs/system-overview.md`
- `docs/platform-security.md`
- `docs/keycloak-frontend-roles.md`
- `docs/security-validation.md`
- `docs/student-guardian-model.md`
- `docs/testing-quality.md`
