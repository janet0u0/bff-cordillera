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
    @DisplayName("ROL NULL → reporte básico anónimo")
    void crearReporteRolNull() {
        DashboardDTO dto = reporteFactory.crearReporte(null, true, datosReales);

        assertNotNull(dto);
        assertEquals("BÁSICO", dto.getTipoReporte());
        assertEquals("ANÓNIMO", dto.getRol());
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("GRIS", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("ROL DESCONOCIDO → caso default")
    void crearReporteRolDesconocido() {
        DashboardDTO dto = reporteFactory.crearReporte("INVITADO_TEMPORAL", true, datosReales);

        assertNotNull(dto);
        assertEquals("BÁSICO", dto.getTipoReporte());
        assertEquals("INVITADO_TEMPORAL", dto.getRol());
        assertEquals("GRIS", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("ANALISTA en tiempo real → VERDE")
    void crearReporteAnalistaTiempoRealVerde() {
        datosReales.put("totalVentas", 12000000.0);
        datosReales.put("transaccionesHoy", 500);

        DashboardDTO dto = reporteFactory.crearReporte("ANALISTA", true, datosReales);

        assertNotNull(dto);
        assertEquals("ANALÍTICO", dto.getTipoReporte());
        assertEquals("ANALISTA", dto.getRol());
        assertTrue(dto.getDatosEnTiempoReal());
        assertEquals("VERDE", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("ANALISTA en caché → AMARILLO")
    void crearReporteAnalistaResilienciaAmarillo() {
        DashboardDTO dto = reporteFactory.crearReporte("ANALISTA", false, datosReales);

        assertNotNull(dto);
        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("AMARILLO", dto.getEstadoGeneral());
        assertEquals(342, dto.getTransaccionesHoy());
    }

    @Test
    @DisplayName("EJECUTIVO tiempo real → VERDE")
    void crearReporteEjecutivoTiempoRealVerde() {
        datosReales.put("totalVentas", 11000000.0);

        DashboardDTO dto = reporteFactory.crearReporte("EJECUTIVO", true, datosReales);

        assertNotNull(dto);
        assertEquals("ESTRATÉGICO", dto.getTipoReporte());
        assertEquals("VERDE", dto.getEstadoGeneral());
        assertTrue(dto.getDatosEnTiempoReal());
    }

    @Test
    @DisplayName("EJECUTIVO caché → AMARILLO")
    void crearReporteEjecutivoCacheAmarillo() {
        DashboardDTO dto = reporteFactory.crearReporte("EJECUTIVO", false, datosReales);

        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("ADMIN SISTEMA tiempo real → VERDE")
    void crearReporteAdminSistemaTiempoReal() {
        datosReales.put("totalVentas", 13000000.0);

        DashboardDTO dto = reporteFactory.crearReporte("ADMIN_SISTEMA", true, datosReales);

        assertEquals("CONTROL TÉCNICO", dto.getTipoReporte());
        assertEquals("VERDE", dto.getEstadoGeneral());
        assertTrue(dto.getDatosEnTiempoReal());
    }

    @Test
    @DisplayName("ADMIN SISTEMA caché → fallback activo")
    void crearReporteAdminSistemaResiliencia() {
        DashboardDTO dto = reporteFactory.crearReporte("ADMIN_SISTEMA", false, datosReales);

        assertFalse(dto.getDatosEnTiempoReal());
        assertEquals("⚠ Circuit Breaker Activado", dto.getMensaje());
    }

    @Test
    @DisplayName("SUPERVISOR → estado ROJO por baja venta")
    void crearReporteSupervisorRojo() {
        datosReales.put("totalVentas", 125000.0);

        DashboardDTO dto = reporteFactory.crearReporte("SUPERVISOR", true, datosReales);

        assertEquals("OPERATIVO", dto.getTipoReporte());
        assertEquals("ROJO", dto.getEstadoGeneral());
    }

    @Test
    @DisplayName("SUPERVISOR → estado AMARILLO por cumplimiento medio")
    void crearReporteSupervisorAmarillo() {
        datosReales.put("totalVentas", 10000000.0);

        DashboardDTO dto = reporteFactory.crearReporte("SUPERVISOR", true, datosReales);

        assertEquals("AMARILLO", dto.getEstadoGeneral());
    }
    @Test
void crearReporteAdminSistemaCasoBajo() {
    Map<String, Object> datos = new HashMap<>();
    datos.put("totalVentas", 1000000.0); // valor bajo para romper la rama VERDE

    ReporteFactory factory = new ReporteFactory();

    DashboardDTO dto = factory.crearReporte(
        "ADMIN_SISTEMA",
        true,
        datos
    );

    assertNotNull(dto);
    assertEquals("ADMIN_SISTEMA", dto.getRol());
}
}