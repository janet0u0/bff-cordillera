package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * PATRÓN FACTORY METHOD - Grupo Cordillera
 * =====================================
 * Centraliza la creación de Dashboards personalizados por rol.
 */
@Component
public class ReporteFactory {

    /**
     * Método fábrica principal.
     */
    public DashboardDTO crearReporte(String rol, boolean datosEnTiempoReal) {
        if (rol == null) return crearReporteBase("ANÓNIMO", datosEnTiempoReal);

        return switch (rol.toUpperCase()) {
            case "EJECUTIVO"     -> crearReporteEjecutivo(datosEnTiempoReal);
            case "ANALISTA"      -> crearReporteAnalista(datosEnTiempoReal);
            case "SUPERVISOR"    -> crearReporteSupervisor(datosEnTiempoReal);
            case "ADMIN_SISTEMA" -> crearReporteAdminSistema(datosEnTiempoReal); // Agregamos tu nuevo rol
            default              -> crearReporteBase(rol, datosEnTiempoReal);
        };
    }

    /**
     * EJECUTIVO: Foco en Rentabilidad y Finanzas.
     */
    private DashboardDTO crearReporteEjecutivo(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("ESTRATÉGICO")
                .rol("EJECUTIVO")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje(datosEnTiempoReal ? "Dashboard Ejecutivo - Tiempo Real" : "⚠ Datos en Caché")
                .ventasTotales(125000.0)
                .rentabilidadNeta(32500.0)
                .estadoGeneral("AMARILLO")
                .build();
    }

    /**
     * ANALISTA: Foco en KPIs y Stock Global.
     */
    private DashboardDTO crearReporteAnalista(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("ANALÍTICO")
                .rol("ANALISTA")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje("Análisis Global de Inventario")
                .ventasTotales(125000.0)
                .productosStockCritico(12)
                .transaccionesHoy(342)
                .estadoGeneral("AMARILLO")
                .build();
    }

    /**
     * SUPERVISOR: Foco en Operación Local.
     */
    private DashboardDTO crearReporteSupervisor(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("OPERATIVO")
                .rol("SUPERVISOR")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje("Control de Sucursal: Santiago Centro")
                .ventasTotales(45000.0)
                .productosStockCritico(25)
                .transaccionesHoy(98)
                .estadoGeneral("ROJO")
                .build();
    }

    /**
     * ADMIN_SISTEMA: Tu nuevo rol - Foco en Salud del Sistema.
     */
    private DashboardDTO crearReporteAdminSistema(boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("CONTROL TÉCNICO")
                .rol("ADMIN_SISTEMA")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)
                .mensaje(datosEnTiempoReal ? "Todos los microservicios sincronizados" : "⚠ Circuit Breaker Activado")
                .estadoGeneral("VERDE")
                .build();
    }

    private DashboardDTO crearReporteBase(String rol, boolean datosEnTiempoReal) {
        return DashboardDTO.builder()
                .tipoReporte("BÁSICO")
                .rol(rol)
                .generadoEn(LocalDateTime.now())
                .mensaje("Acceso Limitado. Contacte a soporte.")
                .build();
    }
}