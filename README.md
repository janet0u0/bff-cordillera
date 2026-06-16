# 🏔️ BFF Cordillera - Backend For Frontend

El **BFF Cordillera** es el punto central de la arquitectura de microservicios. Su función es **orquestar los microservicios**, consolidar respuestas y adaptar la información según el rol del usuario.

Además, actúa como **entrypoint del sistema mediante Docker**, permitiendo levantar toda la arquitectura desde este componente.

---

## 🛠️ Tecnologías

- Java 17  
- Spring Boot 3.3.5  
- Maven Wrapper (`mvnw`)  
- Resilience4j (Circuit Breaker)  
- Spring Boot Actuator  
- JUnit 5  
- Mockito  
- JaCoCo (cobertura de tests)  
- Docker & Docker Compose  
- Lombok  

---

## 🏗️ Arquitectura del sistema

```text
Frontend
   ↓
BFF Cordillera (8084)
   ├── MS-KPI (8082)
   ├── MS-Datos (8083)
   └── MS-Usuarios (8081)
```
```
🧠 Patrones de diseño
Backend For Frontend (BFF)
Factory Pattern (dashboards por rol)
Circuit Breaker (Resilience4j)
DTO Pattern
Client Layer
```
📂 Estructura del proyecto
bff-cordillera/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/cordillera/bffcordillera/
│   │   │   ├── client/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── service/
│   │   │   └── BffCordilleraApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/cordillera/bffcordillera/
│       │   ├── client/
│       │   ├── controller/
│       │   ├── service/
│       │   └── BffCordilleraApplicationTests.java
│       └── resources/
│           └── application-test.properties
├── docker-compose.yml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```
```
📊 Cobertura de tests (JaCoCo)
Global: ~99%
Service: 100%
Controller: 100%
Client: 100%
Factory: 99%
Main: cobertura parcial (normal en Spring Boot)
🚀 Ejecución del proyecto
🐳 Docker (RECOMENDADO)
docker compose up --build

✔ Levanta todo el sistema:

BFF Cordillera
MS-KPI
MS-Datos
MS-Usuarios
💻 Ejecución local
.\mvnw spring-boot:run
🧪 Tests + JaCoCo
.\mvnw test jacoco:report

📄 Reporte generado en:

target/site/jacoco/index.html
🔗 Endpoints
Método	Endpoint	Descripción
GET	/api/bff/dashboard?rol=EJECUTIVO	Dashboard ejecutivo
GET	/api/bff/dashboard?rol=ANALISTA	Dashboard analista
GET	/api/bff/dashboard?rol=SUPERVISOR	Dashboard supervisor
GET	/api/bff/dashboard?rol=ADMIN_SISTEMA	Dashboard administrador
GET	/api/bff/health	Estado del BFF
GET	/api/bff/estado	Estado de microservicios
👥 Roles
EJECUTIVO → estratégico
ANALISTA → analítico
SUPERVISOR → operativo
ADMIN_SISTEMA → control técnico
🔄 Circuit Breaker
CLOSED → funcionamiento normal
OPEN → fallback activo (caché)
HALF-OPEN → recuperación del sistema
🧪 Testing
Unit tests (Service)
Integration tests (Controller)
Client tests (Mock HTTP)
Factory tests (reglas de negocio)
🐳 Docker
Build
docker build -t bff-cordillera .
Run
docker run -p 8084:8084 bff-cordillera
📡 Actuator
http://localhost:8084/actuator/health
http://localhost:8084/actuator/info
http://localhost:8084/actuator/circuitbreakers
