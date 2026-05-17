# Plataforma y seguridad

Esta guia describe los componentes transversales de plataforma utilizados por AulaBOH:

- PostgreSQL para persistencia de los servicios de dominio.
- Eureka Server para registro y descubrimiento de servicios.
- API Gateway como punto de entrada centralizado.
- Keycloak como proveedor de autenticacion, sesiones y roles.
- Actuator para verificacion de salud de los componentes backend.

## Descubrimiento de servicios

Los siguientes componentes se registran en Eureka:

- `students-service`
- `attendance-service`
- `grades-service`
- `bff`
- `api-gateway`

Configuracion base:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.enabled=true
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

## Orden de ejecucion

1. PostgreSQL
2. Keycloak
3. Discovery Server
4. Students Service
5. Attendance Service
6. Grades Service
7. BFF
8. API Gateway
9. Frontend

## PostgreSQL

```powershell
cd platform/database
docker compose up -d
```

Bases provisionadas:

- `aulaboh_students`
- `aulaboh_attendance`
- `aulaboh_grades`

## Discovery Server

```powershell
cd platform/discovery-server
mvn spring-boot:run
```

Panel de servicios:

```text
http://localhost:8761
```

Health check:

```text
http://localhost:8761/actuator/health
```

## API Gateway

```powershell
cd platform/api-gateway
mvn spring-boot:run
```

Health check:

```text
http://localhost:8090/actuator/health
```

Rutas principales:

```text
http://localhost:8090/api/bff/**
```

Los microservicios de dominio no se exponen directamente a traves del Gateway; el acceso externo se centraliza en el BFF.

## Keycloak

```powershell
cd platform/keycloak
docker compose up -d
```

Consola administrativa:

```text
http://localhost:8089
```

Credenciales administrativas locales:

```text
admin / admin
```

## Verificacion

En Eureka deben aparecer las instancias:

- `STUDENTS-SERVICE`
- `ATTENDANCE-SERVICE`
- `GRADES-SERVICE`
- `BFF`
- `API-GATEWAY`

En Keycloak debe existir el realm `aulaboh`, con los clientes `aulaboh-frontend` y `aulaboh-bff`.
