package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.client.MicroservicioClient;
import com.cordillera.bffcordillera.dto.DashboardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio principal del BFF - Grupo Cordillera
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BffService {

    private final ReporteFactory reporteFactory;
    private final MicroservicioClient microservicioClient;

    /**
     * Genera el dashboard según el rol del usuario.
     * Consulta microservicios con Circuit Breaker activo.
     */
    public DashboardDTO generarDashboard(String rol) {
        log.info("Generando dashboard para rol: {}", rol);

        // 1. Consultar microservicios (Circuit Breaker activo)
        Map<String, Object> kpiData   = microservicioClient.obtenerKpis();
        Map<String, Object> datosData = microservicioClient.obtenerDatos();

        // 2. Verificar estado de sincronización
        boolean kpiActualizado    = Boolean.TRUE.equals(kpiData.get("_actualizado"));
        boolean datosActualizado  = Boolean.TRUE.equals(datosData.get("_actualizado"));
        boolean datosEnTiempoReal = kpiActualizado && datosActualizado;

        // 3. MEZCLAR DATOS: Aquí combinamos todo en un solo mapa para la Factory
        Map<String, Object> todosLosDatos = new HashMap<>();
        todosLosDatos.putAll(kpiData);
        todosLosDatos.putAll(datosData);

        log.info("Estado - KPI: {} | Datos: {} | TiempoReal: {}", 
                kpiActualizado, datosActualizado, datosEnTiempoReal);

        // 4. FACTORY METHOD: Ahora le pasamos el mapa 'todosLosDatos' 
        // para que use los 10 millones de Postman en lugar de datos fijos.
        DashboardDTO dashboard = reporteFactory.crearReporte(rol, datosEnTiempoReal, todosLosDatos);

        log.info("Dashboard generado exitosamente para rol: {}", rol);
        return dashboard;
    }
}