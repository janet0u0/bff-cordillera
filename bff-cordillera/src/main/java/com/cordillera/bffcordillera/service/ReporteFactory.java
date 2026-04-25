package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * PATRÓN FACTORY METHOD
 * =====================
 * Crea el tipo correcto de DashboardDTO según el rol del usuario.
 * El Controller no necesita saber cómo se construye cada reporte.
 *
 * Roles:
 *  - EJECUTIVO:  Vista financiera de alto nivel
 *  - ANALISTA:   Vista detallada con stock y transacciones
 *  - SUPERVISOR: Vista operativa
 */
@Component
public class ReporteFactory {

    /**
     * Método fábrica principal.
     * Selecciona el tipo de reporte según el rol.
     */
    public DashboardDTO crearReporte(String rol, boolean datosEnTiempoReal) {
        return switch (rol.toUpperCase()) {
            case "EJECUTIVO"  -> crearReporteEjecutivo(datosEnTiempoReal);
            case "ANALISTA"   -> crearReporteAnalista(datosEnTiempoReal);
            case "SUPERVISOR" -> crearReporteSupervisor(datosEnTiempoReal);
            default           -> crearReporteBase(rol, datosEnTiempoReal);
        };
    }

    // EJECUTIVO: ve KPIs + rentabilidad, NO ve stock ni transacciones
    private DashboardDTO crearReporteEjecutivo(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("EJECUTIVO")
                .rol("EJECUTIVO")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje(datosEnTiempoReal
                        ? "Dashboard ejecutivo en tiempo real"
                        : "⚠ Mostrando últimos datos disponibles")
                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)
                .estadoGeneral("AMARILLO")
                .rentabilidadNeta(32500.0)
                .productosStockCritico(null)
                .transaccionesHoy(null)
                .build();
    }

    // ANALISTA: ve KPIs + stock + transacciones, NO ve rentabilidad
    private DashboardDTO crearReporteAnalista(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("ANALISTA")
                .rol("ANALISTA")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje(datosEnTiempoReal
                        ? "Vista analítica actualizada"
                        : "⚠ Datos en caché")
                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)
                .estadoGeneral("AMARILLO")
                .rentabilidadNeta(null)
                .productosStockCritico(12)
                .transaccionesHoy(342)
                .build();
    }

    // SUPERVISOR: ve KPIs operativos + stock + transacciones
    private DashboardDTO crearReporteSupervisor(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("SUPERVISOR")
                .rol("SUPERVISOR")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje(datosEnTiempoReal
                        ? "Vista supervisión activa"
                        : "⚠ Reconectando con microservicios")
                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)
                .estadoGeneral("AMARILLO")
                .rentabilidadNeta(null)
                .productosStockCritico(12)
                .transaccionesHoy(342)
                .build();
    }

    // Rol no reconocido
    private DashboardDTO crearReporteBase(String rol, boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("BASE")
                .rol(rol)
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje("Rol no reconocido. Acceso limitado.")
                .build();
    }
}