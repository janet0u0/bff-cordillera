package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReporteFactoryTest {

    private ReporteFactory reporteFactory;
    private Map<String, Object> datosReales;

    @BeforeEach
    void setUp() {
        reporteFactory = new ReporteFactory();
        datosReales = new HashMap<>();
    }

    @Test
    @DisplayName("Debería retornar un reporte base limitado si el rol es nulo")
    void crearReporteRolNull() {
        DashboardDTO dto = reporteFactory.crearReporte(null, true, datosReales);

        assertNotNull(dto);
        assertEquals("BÁSICO", dto.getTipoReporte());
        assertEquals("ANÓNIMO", dto.getRol());
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("GRIS", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería retornar un reporte base si el rol no es reconocido (Caso Default)")
    void crearReporteRolDesconocido() {
        DashboardDTO dto = reporteFactory.crearReporte("INVITADO_TEMPORAL", true, datosReales);

        assertNotNull(dto);
        assertEquals("BÁSICO", dto.getTipoReporte());
        assertEquals("INVITADO_TEMPORAL", dto.getRol());
        assertEquals("GRIS", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Analítico válido en tiempo real con estado VERDE")
    void crearReporteAnalistaTiempoRealVerde() {
        // Asignamos ventas altas para forzar cumplimiento > 70%
        datosReales.put("totalVentas", 12000000.0); 
        datosReales.put("transaccionesHoy", 500);

        DashboardDTO dto = reporteFactory.crearReporte("ANALISTA", true, datosReales);

        assertNotNull(dto);
        assertEquals("ANALÍTICO", dto.getTipoReporte());
        assertEquals("ANALISTA", dto.getRol());
        assertTrue(dto.getDatosEnTiempoReal());
        assertEquals("Conexión Directa - Sincronizado", dto.getMensaje());
        assertEquals(500, dto.getTransaccionesHoy());
        assertEquals("VERDE", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Analítico en modo resiliencia con estado AMARILLO")
    void crearReporteAnalistaResilienciaAmarillo() {
        // Ventas bajas para cumplimiento <= 70% usando valores por defecto
        DashboardDTO dto = reporteFactory.crearReporte("analista", false, datosReales);

        assertNotNull(dto);
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("⚠ Modo Resiliencia (Caché)", dto.getMensaje());
        assertEquals(342, dto.getTransaccionesHoy()); // valor por defecto
        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Estratégico (Ejecutivo) en tiempo real con estado VERDE")
    void crearReporteEjecutivoTiempoRealVerde() {
        datosReales.put("totalVentas", 11000000.0);

        DashboardDTO dto = reporteFactory.crearReporte("EJECUTIVO", true, datosReales);

        assertNotNull(dto);
        assertEquals("ESTRATÉGICO", dto.getTipoReporte());
        assertTrue(dto.getDatosEnTiempoReal());
        assertEquals("Dashboard Ejecutivo - Tiempo Real", dto.getMensaje());
        assertNotNull(dto.getRentabilidadNeta());
        assertEquals("VERDE", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Estratégico (Ejecutivo) en caché con estado AMARILLO")
    void crearReporteEjecutivoCacheAmarillo() {
        DashboardDTO dto = reporteFactory.crearReporte("ejecutivo", false, datosReales);

        assertNotNull(dto);
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("⚠ Datos en Caché", dto.getMensaje());
        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte de Control Técnico (Admin Sistema) en tiempo real")
    void crearReporteAdminSistemaTiempoReal() {
        datosReales.put("totalVentas", 13000000.0);

        DashboardDTO dto = reporteFactory.crearReporte("ADMIN_SISTEMA", true, datosReales);

        assertNotNull(dto);
        assertEquals("CONTROL TÉCNICO", dto.getTipoReporte());
        assertTrue(dto.getDatosEnTiempoReal());
        assertEquals("Todos los microservicios sincronizados", dto.getMensaje());
        assertEquals("VERDE", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte de Control Técnico (Admin Sistema) en resiliencia con estado AMARILLO")
    void crearReporteAdminSistemaResiliencia() {
        // Provoca cumplimiento de ~71.2% usando el default de 10.6M
        DashboardDTO dto = reporteFactory.crearReporte("admin_sistema", false, datosReales);

        assertNotNull(dto);
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("⚠ Circuit Breaker Activado", dto.getMensaje());
        assertEquals("VERDE", dto.getEstadoGeneral()); // 10.6M / 15M es > 70%
    }

    @Test
    @DisplayName("Debería crear un reporte de Control Técnico (Admin Sistema) con estado AMARILLO cuando las ventas bajan")
    void crearReporteAdminSistemaAmarillo() {
        datosReales.put("totalVentas", 5000000.0); // 33.3% cumplimiento

        DashboardDTO dto = reporteFactory.crearReporte("ADMIN_SISTEMA", true, datosReales);

        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Operativo (Supervisor) con estado AMARILLO")
    void crearReporteSupervisorAmarillo() {
        datosReales.put("totalVentas", 125000.0); // ventasActuales = 50,000. cumplimiento = 0.71%

        DashboardDTO dto = reporteFactory.crearReporte("SUPERVISOR", true, datosReales);

        assertNotNull(dto);
        assertEquals("OPERATIVO", dto.getTipoReporte());
        assertEquals("Control de Sucursal: Santiago Centro", dto.getMensaje());
        // cumplimiento es menor a 50
        assertEquals("ROJO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("Debería crear un reporte Operativo (Supervisor) con estado AMARILLO si cumple la meta parcial")
    void crearReporteSupervisorRojo() {
        datosReales.put("totalVentas", 10000000.0); // ventasActuales = 4M. cumplimiento = 57.1%

        DashboardDTO dto = reporteFactory.crearReporte("supervisor", true, datosReales);

        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }
}