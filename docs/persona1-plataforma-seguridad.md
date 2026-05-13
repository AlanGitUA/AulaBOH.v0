# Persona 1 - Plataforma y Seguridad

## Responsabilidad

La Persona 1 se encarga de los componentes transversales de plataforma:

- Eureka Server para descubrimiento y monitoreo de microservicios.
- API Gateway como entrada central de la plataforma.
- Keycloak como componente preparado para autenticación, sesiones y roles.
- Logs básicos de ejecución para evidencia técnica.
- Documentación de ejecución de plataforma.

## Cambios realizados para Eureka

Se habilitó el registro de servicios en Eureka en:

- `services/students-service/src/main/resources/application.properties`
- `services/attendance-service/src/main/resources/application.properties`
- `services/grades-service/src/main/resources/application.properties`
- `apps/bff/src/main/resources/application.properties`
- `platform/api-gateway/src/main/resources/application.yml`

Configuración aplicada:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

## Orden correcto de ejecución

1. Eureka Server
2. Students Service
3. Attendance Service
4. Grades Service
5. BFF
6. API Gateway
7. Frontend

## Comandos

### Eureka Server

```powershell
cd "C:\proyectos\AulaBOH.v0\platform\discovery-server"
mvn spring-boot:run
```

Panel:

```text
http://localhost:8761
```

### API Gateway

```powershell
cd "C:\proyectos\AulaBOH.v0\platform\api-gateway"
mvn spring-boot:run
```

Health:

```text
http://localhost:8090/actuator/health
```

### Keycloak

```powershell
cd "C:\proyectos\AulaBOH.v0\platform\keycloak"
docker compose up -d
```

Panel:

```text
http://localhost:8089
```

Credenciales:

```text
admin / admin
```

## Evidencia esperada

En Eureka deben aparecer las instancias:

- `STUDENTS-SERVICE`
- `ATTENDANCE-SERVICE`
- `GRADES-SERVICE`
- `BFF`
- `API-GATEWAY`

En Keycloak debe existir el realm `aulaboh`, con clientes `aulaboh-frontend` y `aulaboh-bff`.

## Explicación para defensa

Eureka permite monitorear qué servicios están activos y prepara la arquitectura para descubrimiento de servicios. Keycloak queda incorporado como componente de seguridad para autenticación centralizada, manejo de sesiones y control de roles. Los logs permiten dejar trazabilidad básica de eventos durante la ejecución.
