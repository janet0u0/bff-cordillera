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

        if (rol == null) {
            return crearReporteBase("ANÓNIMO", datosEnTiempoReal);
        }

        return switch (rol.toUpperCase()) {

            case "EJECUTIVO" ->
                    crearReporteEjecutivo(datosEnTiempoReal);

            case "ANALISTA" ->
                    crearReporteAnalista(datosEnTiempoReal);

            case "SUPERVISOR" ->
                    crearReporteSupervisor(datosEnTiempoReal);

            case "ADMIN_SISTEMA" ->
                    crearReporteAdminSistema(datosEnTiempoReal);

            default ->
                    crearReporteBase(rol, datosEnTiempoReal);
        };
    }

    /**
     * EJECUTIVO:
     * Dashboard enfocado en rentabilidad y finanzas.
     */
    private DashboardDTO crearReporteEjecutivo(boolean datosEnTiempoReal) {

        return DashboardDTO.builder()
                .tipoReporte("ESTRATÉGICO")
                .rol("EJECUTIVO")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)

                .mensaje(datosEnTiempoReal
                        ? "Dashboard Ejecutivo - Tiempo Real"
                        : "⚠ Datos en Caché")

                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)

                .rentabilidadNeta(32500.0)

                .estadoGeneral("AMARILLO")
                .build();
    }

    /**
     * ANALISTA:
     * Dashboard enfocado en KPIs y análisis global.
     */
    private DashboardDTO crearReporteAnalista(boolean datosEnTiempoReal) {

        return DashboardDTO.builder()
                .tipoReporte("ANALÍTICO")
                .rol("ANALISTA")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)

                .mensaje("Análisis Global de Inventario")

                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)

                .productosStockCritico(12)
                .transaccionesHoy(342)

                .estadoGeneral("AMARILLO")
                .build();
    }

    /**
     * SUPERVISOR:
     * Dashboard enfocado en operación local.
     */
    private DashboardDTO crearReporteSupervisor(boolean datosEnTiempoReal) {

        return DashboardDTO.builder()
                .tipoReporte("OPERATIVO")
                .rol("SUPERVISOR")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)

                .mensaje("Control de Sucursal: Santiago Centro")

                .ventasTotales(45000.0)
                .metaVentas(70000.0)
                .porcentajeCumplimiento(64.2)

                .productosStockCritico(25)
                .transaccionesHoy(98)

                .estadoGeneral("ROJO")
                .build();
    }

    /**
     * ADMIN_SISTEMA:
     * Dashboard enfocado en monitoreo técnico del sistema.
     */
    private DashboardDTO crearReporteAdminSistema(boolean datosEnTiempoReal) {

        return DashboardDTO.builder()
                .tipoReporte("CONTROL TÉCNICO")
                .rol("ADMIN_SISTEMA")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)

                .mensaje(datosEnTiempoReal
                        ? "Todos los microservicios sincronizados"
                        : "⚠ Circuit Breaker Activado")

                // KPIs principales
                .ventasTotales(125000.0)
                .metaVentas(150000.0)
                .porcentajeCumplimiento(83.3)

                // Estado técnico
                .estadoGeneral("VERDE")

                // Métricas adicionales
                .rentabilidadNeta(32500.0)
                .productosStockCritico(5)
                .transaccionesHoy(342)

                .build();
    }

    /**
     * Dashboard base para roles no reconocidos.
     */
    private DashboardDTO crearReporteBase(
            String rol,
            boolean datosEnTiempoReal) {

        return DashboardDTO.builder()
                .tipoReporte("BÁSICO")
                .rol(rol)
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(datosEnTiempoReal)

                .mensaje("Acceso Limitado. Contacte a soporte.")

                .ventasTotales(0.0)
                .metaVentas(0.0)
                .porcentajeCumplimiento(0.0)

                .estadoGeneral("GRIS")
                .build();
    }
}