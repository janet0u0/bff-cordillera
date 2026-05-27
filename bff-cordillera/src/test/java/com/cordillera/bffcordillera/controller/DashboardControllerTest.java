package com.cordillera.bffcordillera.controller;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import com.cordillera.bffcordillera.service.BffService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BffController.class)
class BffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BffService bffService;

    @Test
    @DisplayName("GET /api/bff/dashboard - Debería retornar el Dashboard adaptado al rol")
    void obtenerDashboardExito() throws Exception {
        // Arrange
        DashboardDTO mockDashboard = DashboardDTO.builder()
                .tipoReporte("ESTRATÉGICO")
                .rol("EJECUTIVO")
                .generadoEn(LocalDateTime.now())
                .datosEnTiempoReal(true)
                .ventasTotales(125000.0)
                .estadoGeneral("VERDE")
                .build();

        when(bffService.generarDashboard("EJECUTIVO")).thenReturn(mockDashboard);

        // Act & Assert
        // ✅ Usamos Objects.requireNonNull para asegurar al compilador que jamás será nulo
        mockMvc.perform(get("/api/bff/dashboard")
                        .param("rol", "EJECUTIVO")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("EJECUTIVO"))
                .andExpect(jsonPath("$.tipoReporte").value("ESTRATÉGICO"))
                .andExpect(jsonPath("$.estadoGeneral").value("VERDE"));
    }

    @Test
    @DisplayName("GET /api/bff/health - Debería responder con texto de confirmación simple")
    void healthExito() throws Exception {
        mockMvc.perform(get("/api/bff/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("BFF Cordillera funcionando correctamente en puerto 8084"));
    }

    @Test
    @DisplayName("GET /api/bff/estado - Debería retornar el mapa con el estatus de los microservicios")
    void estadoExito() throws Exception {
        mockMvc.perform(get("/api/bff/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bff").value("UP"))
                .andExpect(jsonPath("$.ms-kpi").value("http://localhost:8082"))
                .andExpect(jsonPath("$.ms-datos").value("http://localhost:8083"));
    }
}