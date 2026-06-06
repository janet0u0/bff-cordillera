# 🏔️ BFF Cordillera - Backend For Frontend

Backend For Frontend del Grupo Cordillera. Orquesta los microservicios y adapta la respuesta según el rol del usuario.

## 🛠️ Tecnologías
- Java 17
- Spring Boot 3.3.5
- Resilience4j (Circuit Breaker)
- Spring Actuator
- Docker
- Lombok
- Maven

## 🎯 Patrones Aplicados
- **Factory Method**: Crea dashboards personalizados según el rol del usuario
- **Circuit Breaker**: Si MS-KPI o MS-Datos fallan, retorna datos del caché automáticamente
- **DTO Pattern**: Separa la respuesta de la lógica interna

## 🏗️ Arquitectura
```text
Frontend (3000)
      ↓
BFF (8084)
  ↓       ↓
MS-KPI  MS-Datos
(8082)   (8083)
MS-Usuarios (8081)
```

## ✅ Requisitos
- Java 17
- Docker Desktop
- Maven

## 🚀 Instalación y Ejecución

### Opción 1: Docker (recomendado)
```bash
docker compose up --build
```
Levanta todo el sistema automáticamente.

### Opción 2: Local

**1. Clonar el repositorio**
```bash
git clone https://github.com/janet0u0/bff-cordillera
cd bff-cordillera
```

**2. Levantar los microservicios**
```bash
cd ms-usuarios && .\mvnw spring-boot:run
cd ms-kpi && .\mvnw spring-boot:run
cd ms-datos && .\mvnw spring-boot:run
```

**3. Ejecutar el BFF**
```bash
.\mvnw spring-boot:run
```
Disponible en `http://localhost:8084`

## 🔗 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/bff/dashboard?rol=EJECUTIVO | Dashboard ejecutivo |
| GET | /api/bff/dashboard?rol=ANALISTA | Dashboard analista |
| GET | /api/bff/dashboard?rol=SUPERVISOR | Dashboard supervisor |
| GET | /api/bff/dashboard?rol=ADMIN_SISTEMA | Dashboard administrador |
| GET | /api/bff/health | Estado del BFF |
| GET | /api/bff/estado | Estado de microservicios |

## 👥 Roles disponibles

| Rol | Tipo Reporte | Descripción |
|-----|-------------|-------------|
| EJECUTIVO | ESTRATÉGICO | Foco en rentabilidad y finanzas |
| ANALISTA | ANALÍTICO | Foco en KPIs y stock global |
| SUPERVISOR | OPERATIVO | Foco en operación local |
| ADMIN_SISTEMA | CONTROL TÉCNICO | Salud del sistema |

## 🔄 Circuit Breaker

Si MS-KPI o MS-Datos fallan, el sistema retorna automáticamente datos del caché sin interrumpir al usuario.

| Estado | Descripción |
|--------|-------------|
| CERRADO | Todo funciona, llamadas normales |
| ABIERTO | Demasiados fallos, retorna caché |
| SEMI-ABIERTO | Probando si el servicio se recuperó |

## 📂 Estructura del Proyecto

```text
bff-cordillera/
├── src/
│   ├── main/
│   │   ├── client/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── service/
│   │   └── resources/
│   └── test/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 📌 Componentes principales

```text
client/       → Comunicación con microservicios
controller/   → Endpoints REST del BFF
dto/          → Objetos de transferencia de datos
service/      → Lógica de negocio y reportes
resources/    → Configuración del sistema
```

## 📡 Monitoreo

```
GET http://localhost:8084/actuator/health
GET http://localhost:8084/actuator/info
GET http://localhost:8084/actuator/circuitbreakers
```
