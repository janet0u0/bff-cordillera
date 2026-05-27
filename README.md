BFF Cordillera - Backend For Frontend

Backend For Frontend del Grupo Cordillera. Orquesta los microservicios y adapta la respuesta según el rol del usuario.

## Tecnologías
- Java 17
- Spring Boot 3.3.5
- Resilience4j (Circuit Breaker)
- Spring Actuator
- Docker
- Lombok
- Maven

## Patrones Aplicados
- **Factory Method**: Crea dashboards personalizados según el rol del usuario
- **Circuit Breaker**: Si MS-KPI o MS-Datos fallan, retorna datos del caché automáticamente
- **DTO Pattern**: Separa la respuesta de la lógica interna

## Arquitectura
Frontend
↓
BFF (puerto 8084)
↓           ↓
MS-KPI      MS-Datos
(8082)       (8083)
↓           ↓
MS-Usuarios (8081)

## Requisitos
- Java 17
- Docker Desktop
- MS-KPI corriendo en puerto 8082
- MS-Datos corriendo en puerto 8083
- MS-Usuarios corriendo en puerto 8081

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd bff-cordillera
```

### 2. Asegurarse que los microservicios estén corriendo
```bash
# MS-Usuarios
cd ms-usuarios && .\mvnw spring-boot:run

# MS-KPI
cd ms-kpi && .\mvnw spring-boot:run

# MS-Datos
cd ms-datos && .\mvnw spring-boot:run
```

### 3. Ejecutar el BFF
```bash
.\mvnw spring-boot:run
```

El servicio quedará disponible en `http://localhost:8084`

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/bff/dashboard?rol=EJECUTIVO | Dashboard ejecutivo |
| GET | /api/bff/dashboard?rol=ANALISTA | Dashboard analista |
| GET | /api/bff/dashboard?rol=SUPERVISOR | Dashboard supervisor |
| GET | /api/bff/dashboard?rol=ADMIN_SISTEMA | Dashboard admin sistema |
| GET | /api/bff/health | Estado del BFF |
| GET | /api/bff/estado | Estado de microservicios |

## Roles disponibles

| Rol | Tipo Reporte | Descripción |
|-----|-------------|-------------|
| EJECUTIVO | ESTRATÉGICO | Foco en rentabilidad y finanzas |
| ANALISTA | ANALÍTICO | Foco en KPIs y stock global |
| SUPERVISOR | OPERATIVO | Foco en operación local |
| ADMIN_SISTEMA | CONTROL TÉCNICO | Salud del sistema |

## Circuit Breaker
Si MS-KPI o MS-Datos fallan, el sistema retorna automáticamente datos del caché sin interrumpir al usuario.

Estados del circuito:
- **CERRADO**: Todo funciona, llamadas normales
- **ABIERTO**: Demasiados fallos, retorna caché
- **SEMI-ABIERTO**: Probando si el servicio se recuperó

## 📂 Estructura del Proyecto BFF-Cordillera

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

## Monitoreo
GET http://localhost:8084/actuator/health
GET http://localhost:8084/actuator/info
GET http://localhost:8084/actuator/circuitbreakers
