package com.cordillera.bffcordillera.service;

import com.cordillera.bffcordillera.client.MicroservicioClient;
import com.cordillera.bffcordillera.dto.DashboardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BffServiceTest {

    @Mock
    private MicroservicioClient microservicioClient;

    @Mock
    private ReporteFactory reporteFactory;

    @InjectMocks
    private BffService bffService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Rama 1 (V & V): KPI y Datos actualizados -> datosEnTiempoReal = true")
    void generarDashboardTiempoRealExito() {
        // Arrange
        Map<String, Object> mockKpi = new HashMap<>();
        mockKpi.put("_actualizado", true);
        mockKpi.put("kpis", new Object[]{"KPI-1"});

        Map<String, Object> mockDatos = new HashMap<>();
        mockDatos.put("_actualizado", true);
        mockDatos.put("totalVentas", 10680499.0);

        DashboardDTO mockDashboard = DashboardDTO.builder().rol("EJECUTIVO").datosEnTiempoReal(true).build();

        when(microservicioClient.obtenerKpis()).thenReturn(mockKpi);
        when(microservicioClient.obtenerDatos()).thenReturn(mockDatos);
        when(reporteFactory.crearReporte(eq("EJECUTIVO"), eq(true), anyMap())).thenReturn(mockDashboard);

        // Act
        DashboardDTO resultado = bffService.generarDashboard("EJECUTIVO");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getDatosEnTiempoReal());
    }

    @Test
    @DisplayName("Rama 2 (F & V): KPI fallido, Datos actualizados -> datosEnTiempoReal = false")
    void generarDashboardKpiFalso() {
        // Arrange
        Map<String, Object> mockKpi = new HashMap<>();
        mockKpi.put("_actualizado", false); // KPI viene de caché

        Map<String, Object> mockDatos = new HashMap<>();
        mockDatos.put("_actualizado", true);

        DashboardDTO mockDashboard = DashboardDTO.builder().rol("ANALISTA").datosEnTiempoReal(false).build();

        when(microservicioClient.obtenerKpis()).thenReturn(mockKpi);
        when(microservicioClient.obtenerDatos()).thenReturn(mockDatos);
        when(reporteFactory.crearReporte(eq("ANALISTA"), eq(false), anyMap())).thenReturn(mockDashboard);

        // Act
        DashboardDTO resultado = bffService.generarDashboard("ANALISTA");

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getDatosEnTiempoReal());
    }

    @Test
    @DisplayName("Rama 3 (V & F): KPI actualizado, Datos fallidos -> datosEnTiempoReal = false")
    void generarDashboardDatosFalso() {
        // Arrange
        Map<String, Object> mockKpi = new HashMap<>();
        mockKpi.put("_actualizado", true);

        Map<String, Object> mockDatos = new HashMap<>();
        mockDatos.put("_actualizado", false); // Datos viene de caché

        DashboardDTO mockDashboard = DashboardDTO.builder().rol("SUPERVISOR").datosEnTiempoReal(false).build();

        when(microservicioClient.obtenerKpis()).thenReturn(mockKpi);
        when(microservicioClient.obtenerDatos()).thenReturn(mockDatos);
        when(reporteFactory.crearReporte(eq("SUPERVISOR"), eq(false), anyMap())).thenReturn(mockDashboard);

        // Act
        DashboardDTO resultado = bffService.generarDashboard("SUPERVISOR");

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getDatosEnTiempoReal());
    }

    @Test
    @DisplayName("Rama 4 (F & F): Ambos caídos -> datosEnTiempoReal = false")
    void generarDashboardAmbosFalsos() {
        // Arrange
        Map<String, Object> mockKpi = new HashMap<>();
        mockKpi.put("_actualizado", false);

        Map<String, Object> mockDatos = new HashMap<>();
        mockDatos.put("_actualizado", false);

        DashboardDTO mockDashboard = DashboardDTO.builder().rol("ADMIN_SISTEMA").datosEnTiempoReal(false).build();

        when(microservicioClient.obtenerKpis()).thenReturn(mockKpi);
        when(microservicioClient.obtenerDatos()).thenReturn(mockDatos);
        when(reporteFactory.crearReporte(eq("ADMIN_SISTEMA"), eq(false), anyMap())).thenReturn(mockDashboard);

        // Act
        DashboardDTO resultado = bffService.generarDashboard("ADMIN_SISTEMA");

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getDatosEnTiempoReal());
    }
}