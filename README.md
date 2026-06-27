## Ejecucion recomendada

Este proyecto puede estar ubicado en cualquier carpeta del computador.
Por eso, todos los comandos deben ejecutarse usando rutas relativas desde la raiz del proyecto.

Primero se debe abrir una terminal en la carpeta principal del proyecto, donde se encuentran las carpetas:

```text
apps/
services/
platform/
packages/
docs/
pom.xml
```

Ejemplo de ubicacion de la raiz del proyecto:

```text
AulaBOH.v0/
```

No es necesario que el proyecto este en una ruta especifica como `C:\proyectos`.
Cada integrante debe ubicarse en la carpeta donde tenga descargado o clonado el proyecto.

## 1. Levantar PostgreSQL

Desde la raiz del proyecto:

```powershell
cd platform\database
docker compose up -d
```

Verificar que el contenedor quedo activo:

```powershell
docker ps
```

Debe aparecer un contenedor relacionado con PostgreSQL, por ejemplo:

```text
aulaboh-postgres
```

## 2. Levantar Keycloak

Abrir otra terminal desde la raiz del proyecto:

```powershell
cd platform\keycloak
docker compose up -d
```

Keycloak queda disponible en:

```text
http://localhost:8089
```

Credenciales del administrador de Keycloak:

```text
Usuario: admin
Contrasena: admin
```

El realm del proyecto se encuentra en:

```text
platform/keycloak/realms/aulaboh-realm.json
```

## 3. Compilar el proyecto desde la raiz

Abrir una nueva terminal en la raiz del proyecto y ejecutar:

```powershell
mvn clean install -DskipTests
```

Este comando compila los modulos sin volver a ejecutar las pruebas.

## 4. Levantar Eureka / Discovery Server

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd platform\discovery-server
mvn spring-boot:run
```

Link principal:

```text
http://localhost:8761
```

En Eureka deberian registrarse posteriormente:

```text
STUDENTS-SERVICE
ATTENDANCE-SERVICE
GRADES-SERVICE
BFF
API-GATEWAY
```

## 5. Levantar Students Service

Puerto: `8081`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd services\students-service
mvn spring-boot:run
```

Links para probar:

```text
http://localhost:8081/actuator/health
http://localhost:8081/api/students
```

## 6. Levantar Attendance Service

Puerto: `8084`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd services\attendance-service
mvn spring-boot:run
```

Links para probar:

```text
http://localhost:8084/actuator/health
http://localhost:8084/api/classes
http://localhost:8084/api/attendances/student/1
http://localhost:8084/api/attendances/course/4A
http://localhost:8084/api/attendances/student/1/summary
```

## 7. Levantar Grades Service

Puerto: `8083`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd services\grades-service
mvn spring-boot:run
```

Links para probar:

```text
http://localhost:8083/actuator/health
http://localhost:8083/api/evaluations
http://localhost:8083/api/grades/student/1
```

## 8. Levantar BFF

Puerto: `8080`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd apps\bff
mvn spring-boot:run
```

Links para probar:

```text
http://localhost:8080/actuator/health
http://localhost:8080/api/bff/students
http://localhost:8080/api/bff/classes
http://localhost:8080/api/bff/evaluations
http://localhost:8080/api/bff/students/1/summary
```

## 9. Levantar API Gateway

Puerto: `8090`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd platform\api-gateway
mvn spring-boot:run
```

Links para probar por Gateway:

```text
http://localhost:8090/actuator/health
http://localhost:8090/api/bff/students
http://localhost:8090/api/bff/classes
http://localhost:8090/api/bff/evaluations
```

El API Gateway expone principalmente las rutas del BFF mediante:

```text
/api/bff/**
```

## 10. Levantar Frontend

Puerto: `5173`

Abrir una terminal nueva desde la raiz del proyecto:

```powershell
cd apps\frontend
npm install
npm run dev
```

Link principal:

```text
http://localhost:5173
```

## Orden completo recomendado

```text
1. PostgreSQL
2. Keycloak
3. Compilacion Maven desde la raiz
4. Eureka / Discovery Server
5. Students Service
6. Attendance Service
7. Grades Service
8. BFF
9. API Gateway
10. Frontend
```

## Importante sobre las rutas

Los comandos estan escritos con rutas relativas para que funcionen en cualquier computador.

Correcto:

```powershell
cd platform\keycloak
docker compose up -d
```

Incorrecto para un README compartido:

```powershell
cd "C:\proyectos\AulaBOH.v0\platform\keycloak"
docker compose up -d
```

Cada integrante solo debe asegurarse de abrir la terminal en la carpeta raiz del proyecto antes de ejecutar los comandos.
