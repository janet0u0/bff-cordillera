package com.cordillera.bffcordillera.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class MicroservicioClientTest {

    private MicroservicioClient microservicioClient;
    private MockRestServiceServer mockServer;
    
    private final String kpiUrl = "http://localhost:8081";
    private final String datosUrl = "http://localhost:8082";

    @BeforeEach
    void setUp() {
        microservicioClient = new MicroservicioClient(kpiUrl, datosUrl);
        RestTemplate restTemplate = (RestTemplate) ReflectionTestUtils.getField(microservicioClient, "restTemplate");
        mockServer = MockRestServiceServer.createServer(Objects.requireNonNull(restTemplate));
    }

    @Test
    @DisplayName("Debería retornar KPIs reales si MS-KPI responde con un array válido")
    void obtenerKpisExito() {
        String jsonResponse = "[\"KPI-1\", \"KPI-2\"]";
        mockServer.expect(requestTo(kpiUrl + "/api/kpis"))
                .andExpect(method(Objects.requireNonNull(HttpMethod.GET)))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        Map<String, Object> resultado = microservicioClient.obtenerKpis();

        mockServer.verify();
        assertTrue((Boolean) resultado.get("_actualizado"));
        assertNotNull(resultado.get("kpis"));
    }

    @Test
    @DisplayName("Rama if: Debería retornar el caché sin modificar si MS-KPI responde un array vacío")
    void obtenerKpisVacio() {
        mockServer.expect(requestTo(kpiUrl + "/api/kpis"))
                .andExpect(method(Objects.requireNonNull(HttpMethod.GET)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        Map<String, Object> resultado = microservicioClient.obtenerKpis();

        mockServer.verify();
        assertFalse((Boolean) resultado.get("_actualizado"));
    }

    @Test
    @DisplayName("Rama if: Debería retornar el caché sin modificar si MS-KPI responde null")
    void obtenerKpisNull() {
        mockServer.expect(requestTo(kpiUrl + "/api/kpis"))
                .andExpect(method(Objects.requireNonNull(HttpMethod.GET)))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        Map<String, Object> resultado = microservicioClient.obtenerKpis();

        mockServer.verify();
        assertFalse((Boolean) resultado.get("_actualizado"));
    }

    @Test
    @DisplayName("Debería retornar Datos de ventas reales si MS-Datos responde exitosamente")
    void obtenerDatosExito() {
        mockServer.expect(requestTo(datosUrl + "/api/datos/ventas/total"))
                .andExpect(method(Objects.requireNonNull(HttpMethod.GET)))
                .andRespond(withSuccess("450000.0", MediaType.APPLICATION_JSON));

        Map<String, Object> resultado = microservicioClient.obtenerDatos();

        mockServer.verify();
        assertTrue((Boolean) resultado.get("_actualizado"));
        assertEquals(450000.0, resultado.get("totalVentas"));
    }

    @Test
    @DisplayName("Rama if: Debería retornar el caché sin modificar si MS-Datos responde null")
    void obtenerDatosNull() {
        mockServer.expect(requestTo(datosUrl + "/api/datos/ventas/total"))
                .andExpect(method(Objects.requireNonNull(HttpMethod.GET)))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        Map<String, Object> resultado = microservicioClient.obtenerDatos();

        mockServer.verify();
        assertFalse((Boolean) resultado.get("_actualizado"));
    }

    @Test
    @DisplayName("Fallback: Forzar ejecución del método kpiFallback manualmente")
    void testKpiFallback() {
        Exception ex = new RuntimeException("Error simulado de pasarela");
        Map<String, Object> resultado = microservicioClient.kpiFallback(ex);

        assertFalse((Boolean) resultado.get("_actualizado"));
        assertEquals("MS-KPI no disponible - mostrando datos en cache", resultado.get("_mensaje"));
    }

    @Test
    @DisplayName("Fallback: Forzar ejecución del método datosFallback manualmente")
    void testDatosFallback() {
        Exception ex = new RuntimeException("Error simulado de persistencia");
        Map<String, Object> resultado = microservicioClient.datosFallback(ex);

        assertFalse((Boolean) resultado.get("_actualizado"));
        assertEquals("MS-Datos no disponible - mostrando datos en cache", resultado.get("_mensaje"));
    }
}