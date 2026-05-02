# BFF Cordillera - Grupo Cordillera

## 📌 Descripción
Backend For Frontend (BFF) de la Plataforma de Monitoreo Inteligente.
Consolida y adapta la información de los microservicios según
el rol del usuario autenticado (Ejecutivo, Analista, Supervisor).

## 🎯 Patrones aplicados
- **Factory Method**: Crea el tipo de reporte correcto según el rol
  del usuario, sin exponer la lógica de construcción al Controller.
- **Circuit Breaker**: Si MS-KPI o MS-Datos fallan, el sistema
  retorna datos del caché sin interrumpir al usuario.

## ⚙️ Tecnologías
- Java 17
- Spring Boot 3.5.14
- Resilience4j (Circuit Breaker)
- Maven
- Lombok

## 📁 Estructura del proyecto
bff-cordillera/
├── controller/  → BffController (endpoints REST)
├── service/     → BffService (orquestación)
│                → ReporteFactory (Factory Method)
├── client/      → MicroservicioClient (Circuit Breaker)
└── dto/         → DashboardDTO (respuesta adaptada por rol)

## 🌐 Endpoints disponibles
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/bff/dashboard?rol=EJECUTIVO | Dashboard ejecutivo |
| GET | /api/bff/dashboard?rol=ANALISTA | Dashboard analista |
| GET | /api/bff/dashboard?rol=SUPERVISOR | Dashboard supervisor |
| GET | /api/bff/health | Estado del BFF |
| GET | /api/bff/estado | Estado de microservicios |

## 📊 Información por rol
| Rol | Ventas | Meta | Rentabilidad | Stock | Transacciones |
|-----|--------|------|--------------|-------|---------------|
| EJECUTIVO | ✅ | ✅ | ✅ | ❌ | ❌ |
| ANALISTA | ✅ | ✅ | ❌ | ✅ | ✅ |
| SUPERVISOR | ✅ | ✅ | ❌ | ✅ | ✅ |

## 🔌 Microservicios que consume
- MS-Usuarios: http://localhost:8081
- MS-KPI:      http://localhost:8082
- MS-Datos:    http://localhost:8083

## ⚡ Circuit Breaker
Si un microservicio falla más del 50% de las llamadas,
el circuito se ABRE y retorna datos del caché automáticamente.

## 🐳 Cómo ejecutar con Docker
```bash
docker-compose up -d
mvn spring-boot:run
```

## 💻 Cómo ejecutar sin Docker
1. Ejecutar: `mvn spring-boot:run`
2. Servidor en: http://localhost:8084

## ✅ Requisitos
- Java 17+
- Maven
- MS-Usuarios, MS-KPI y MS-Datos corriendo

## 👥 Autores
- Janet Huaylla Huayllas
- Bairo Pasten Codoceo