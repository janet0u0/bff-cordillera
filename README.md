# BFF Cordillera - Grupo Cordillera

## Descripción
Backend For Frontend (BFF) de la Plataforma de Monitoreo Inteligente.
Consolida y adapta la información de los microservicios según
el rol del usuario autenticado (Ejecutivo, Analista, Supervisor).

## Patrón aplicado
- **Factory Method**: Crea el tipo de reporte correcto según el rol
  del usuario, sin exponer la lógica de construcción al Controller.

## Tecnologías
- Java 17
- Spring Boot 3.5.14
- Maven
- Lombok

## Estructura del proyecto
bff-cordillera/
├── controller/  → BffController (endpoints REST)
├── service/     → BffService (orquestación)
│                → ReporteFactory (Factory Method)
└── dto/         → DashboardDTO (respuesta adaptada por rol)

## Endpoints disponibles
- GET /api/bff/dashboard?rol=EJECUTIVO  → Dashboard ejecutivo
- GET /api/bff/dashboard?rol=ANALISTA   → Dashboard analista
- GET /api/bff/dashboard?rol=SUPERVISOR → Dashboard supervisor
- GET /api/bff/health                   → Estado del BFF

## Microservicios que consume
- MS-Datos:    http://localhost:8083
- MS-KPI:      http://localhost:8082
- MS-Usuarios: http://localhost:8081

## Cómo ejecutar
1. Ejecutar: mvn spring-boot:run
2. Servidor en: http://localhost:8084

## Autores
- Janet Huaylla Huayllas
- Bairo Pasten Codoceo