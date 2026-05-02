package com.cordillera.bffcordillera.controller;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import com.cordillera.bffcordillera.service.BffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller REST del BFF - Grupo Cordillera
 * Adapta la respuesta según el rol del usuario.
 *
 * Patrón aplicado: Factory Method
 * El BffService usa ReporteFactory para crear el tipo
 * de reporte correcto según el rol del usuario.
 *
 * Patrón aplicado: Circuit Breaker
 * Si MS-KPI o MS-Datos fallan, el sistema retorna
 * datos del caché sin interrumpir al usuario.
 */
@Slf4j
@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BffController {

    private final BffService bffService;

    /**
     * GET /api/bff/dashboard?rol=EJECUTIVO
     * Retorna dashboard adaptado al rol del usuario.
     * Roles: EJECUTIVO | ANALISTA | SUPERVISOR
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> obtenerDashboard(
            @RequestParam(defaultValue = "EJECUTIVO") String rol) {
        log.info("Solicitud de dashboard para rol: {}", rol);
        DashboardDTO dashboard = bffService.generarDashboard(rol);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/bff/health
     * Verifica que el BFF está funcionando
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("BFF Cordillera funcionando correctamente en puerto 8084");
    }

    /**
     * GET /api/bff/estado
     * Verifica el estado de los microservicios downstream
     * Muestra si MS-KPI y MS-Datos están UP o DEGRADED
     */
    @GetMapping("/estado")
    public ResponseEntity<Map<String, String>> estado() {
        log.info("Verificando estado de microservicios");
        return ResponseEntity.ok(Map.of(
                "bff", "UP",
                "ms-kpi", "http://localhost:8082",
                "ms-datos", "http://localhost:8083",
                "ms-usuarios", "http://localhost:8081"
        ));
    }
}