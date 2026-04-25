package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio principal del BFF
 * Orquesta la consulta a los microservicios
 * y usa ReporteFactory para construir la respuesta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BffService {

    private final ReporteFactory reporteFactory;

    /**
     * Genera el dashboard según el rol del usuario
     */
    public DashboardDTO generarDashboard(String rol) {
        log.info("Generando dashboard para rol: {}", rol);

        boolean datosEnTiempoReal = true;

        DashboardDTO dashboard = reporteFactory.crearReporte(rol, datosEnTiempoReal);

        log.info("Dashboard generado para rol: {}", rol);
        return dashboard;
    }
}