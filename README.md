markdown# BFF Cordillera - Grupo Cordillera

## 📌 Descripción
Backend For Frontend (BFF) de la Plataforma de Monitoreo Inteligente.
Consolida y adapta la información de los microservicios según
el rol del usuario autenticado.

## 🎯 Patrones aplicados
- **Factory Method**: ReporteFactory crea el dashboard correcto según el rol
- **Circuit Breaker**: MicroservicioClient con Resilience4j garantiza disponibilidad

## ⚙️ Tecnologías
- Java 17
- Spring Boot 3.5.14
- Resilience4j (Circuit Breaker)
- Maven
- Lombok

## 📁 Estructura del proyecto
bff-cordillera/
├── controller/
│   └── BffController.java      → Endpoints REST
├── service/
│   ├── BffService.java         → Orquestación
│   └── ReporteFactory.java     → Patrón Factory Method
├── client/
│   └── MicroservicioClient.java → Circuit Breaker
└── dto/
└── DashboardDTO.java        → Respuesta por rol

## 📊 Dashboards por rol
| Rol | Tipo | Ve |
|-----|------|-----|
| EJECUTIVO | ESTRATÉGICO | Ventas + Rentabilidad |
| ANALISTA | ANALÍTICO | Ventas + Stock + Transacciones |
| SUPERVISOR | OPERATIVO | KPIs de su sucursal |
| ADMIN_SISTEMA | CONTROL TÉCNICO | Estado del sistema |

## 🌐 Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/bff/dashboard?rol=EJECUTIVO | Dashboard por rol |
| GET | /api/bff/health | Estado del BFF |
| GET | /api/bff/estado | Estado microservicios |

## ⚡ Circuit Breaker
- Umbral de fallo: 50% en ventana de 10 llamadas
- Estado ABIERTO: retorna datos del caché
- Recuperación automática cada 5 segundos

## 🔌 Microservicios que consume
- MS-Usuarios: http://localhost:8081
- MS-KPI: http://localhost:8082
- MS-Datos: http://localhost:8083

## 🐳 Cómo ejecutar
```bash
docker-compose up -d
mvn spring-boot:run
```

## 💻 Sin Docker
```bash
mvn spring-boot:run
```
Servidor en: http://localhost:8084

## ✅ Requisitos
- Java 17+
- Maven
- MS-Usuarios, MS-KPI y MS-Datos corriendo

## 👥 Autores
- Janet Huaylla Huayllas
- Bairo Pasten Codoceo