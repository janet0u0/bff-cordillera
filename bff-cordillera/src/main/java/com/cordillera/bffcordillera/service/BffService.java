package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.client.MicroservicioClient;
import com.cordillera.bffcordillera.dto.DashboardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Servicio principal del BFF
 * Orquesta la consulta a los microservicios
 * y usa ReporteFactory para construir la respuesta.
 *
 * Si MS-KPI o MS-Datos fallan, el Circuit Breaker
 * retorna datos del caché automáticamente.
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

        // Consultar microservicios (Circuit Breaker activo)
        Map<String, Object> kpiData   = microservicioClient.obtenerKpis();
        Map<String, Object> datosData = microservicioClient.obtenerDatos();

        // Verificar si datos son en tiempo real o del caché
        boolean kpiActualizado    = Boolean.TRUE.equals(kpiData.get("_actualizado"));
        boolean datosActualizado  = Boolean.TRUE.equals(datosData.get("_actualizado"));
        boolean datosEnTiempoReal = kpiActualizado && datosActualizado;

        log.info("Estado - KPI: {} | Datos: {} | TiempoReal: {}",
                kpiActualizado, datosActualizado, datosEnTiempoReal);

        // Factory Method: construir respuesta según el rol
        DashboardDTO dashboard = reporteFactory.crearReporte(rol, datosEnTiempoReal);

        log.info("Dashboard generado para rol: {}", rol);
        return dashboard;
    }
}