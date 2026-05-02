package com.cordillera.bffcordillera.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Cliente HTTP hacia los microservicios MS-KPI y MS-Datos.
 *
 * PATRÓN CIRCUIT BREAKER
 * ======================
 * Si un microservicio falla o supera el 50% de errores,
 * el circuito se ABRE y retorna automáticamente datos
 * del caché en lugar de propagar el error al frontend.
 *
 * Estados del circuito:
 *  - CERRADO:      Todo funciona, llamadas normales
 *  - ABIERTO:      Demasiados fallos, retorna caché
 *  - SEMI-ABIERTO: Probando si el servicio se recuperó
 */
@Slf4j
@Component
public class MicroservicioClient {

    private final RestTemplate restTemplate;
    private final String kpiUrl;
    private final String datosUrl;

    private final Map<String, Object> cacheKpi = new HashMap<>();
    private final Map<String, Object> cacheDatos = new HashMap<>();

    public MicroservicioClient(
            @Value("${microservicio.kpi.url}") String kpiUrl,
            @Value("${microservicio.datos.url}") String datosUrl) {
        this.restTemplate = new RestTemplate();
        this.kpiUrl = kpiUrl;
        this.datosUrl = datosUrl;
        inicializarCache();
    }

    @SuppressWarnings("unchecked")
    @CircuitBreaker(name = "ms-kpi", fallbackMethod = "kpiFallback")
    public Map<String, Object> obtenerKpis() {
        log.info("Consultando KPIs a MS-KPI en: {}", kpiUrl);
        Map<String, Object> response = restTemplate.getForObject(
                kpiUrl + "/api/kpis", Map.class);
        if (response != null) {
            cacheKpi.putAll(response);
            cacheKpi.put("_actualizado", true);
        }
        return cacheKpi;
    }

    @SuppressWarnings("unchecked")
    @CircuitBreaker(name = "ms-datos", fallbackMethod = "datosFallback")
    public Map<String, Object> obtenerDatos() {
        log.info("Consultando datos a MS-Datos en: {}", datosUrl);
        Map<String, Object> response = restTemplate.getForObject(
                datosUrl + "/api/datos/ventas/total", Map.class);
        if (response != null) {
            cacheDatos.putAll(response);
            cacheDatos.put("_actualizado", true);
        }
        return cacheDatos;
    }

    public Map<String, Object> kpiFallback(Exception ex) {
        log.warn("Circuit Breaker ABIERTO para MS-KPI. Causa: {}. Retornando cache.",
                ex.getMessage());
        cacheKpi.put("_actualizado", false);
        cacheKpi.put("_mensaje", "MS-KPI no disponible - mostrando datos en cache");
        return cacheKpi;
    }

    public Map<String, Object> datosFallback(Exception ex) {
        log.warn("Circuit Breaker ABIERTO para MS-Datos. Causa: {}. Retornando cache.",
                ex.getMessage());
        cacheDatos.put("_actualizado", false);
        cacheDatos.put("_mensaje", "MS-Datos no disponible - mostrando datos en cache");
        return cacheDatos;
    }

    private void inicializarCache() {
        cacheKpi.put("ventasTotales", 125000.0);
        cacheKpi.put("metaVentas", 150000.0);
        cacheKpi.put("porcentajeCumplimiento", 83.3);
        cacheKpi.put("estadoGeneral", "AMARILLO");
        cacheKpi.put("_actualizado", false);
        cacheDatos.put("totalVentas", 125000.0);
        cacheDatos.put("transaccionesHoy", 342);
        cacheDatos.put("_actualizado", false);
        log.info("Cache inicializado con datos de demostracion.");
    }
}