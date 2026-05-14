# Plataforma y seguridad

Esta guía describe los componentes transversales de plataforma utilizados por AulaBOH:

- Eureka Server para registro y descubrimiento de servicios.
- API Gateway como punto de entrada centralizado.
- Keycloak como proveedor de autenticación, sesiones y roles.
- Actuator para verificación de salud de los componentes backend.

## Descubrimiento de servicios

Los siguientes componentes se registran en Eureka:

- `students-service`
- `attendance-service`
- `grades-service`
- `bff`
- `api-gateway`

Configuración base:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.enabled=true
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

## Orden de ejecución

1. Discovery Server
2. Students Service
3. Attendance Service
4. Grades Service
5. BFF
6. API Gateway
7. Frontend

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
http://localhost:8090/api/students/**
http://localhost:8090/api/classes/**
http://localhost:8090/api/attendances/**
http://localhost:8090/api/evaluations/**
http://localhost:8090/api/grades/**
```

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

## Verificación

En Eureka deben aparecer las instancias:

- `STUDENTS-SERVICE`
- `ATTENDANCE-SERVICE`
- `GRADES-SERVICE`
- `BFF`
- `API-GATEWAY`

En Keycloak debe existir el realm `aulaboh`, con los clientes `aulaboh-frontend` y `aulaboh-bff`.
