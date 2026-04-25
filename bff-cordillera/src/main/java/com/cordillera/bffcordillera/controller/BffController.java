package com.cordillera.bffcordillera.controller;

import com.cordillera.bffcordillera.dto.DashboardDTO;
import com.cordillera.bffcordillera.service.BffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST del BFF - Grupo Cordillera
 * Adapta la respuesta según el rol del usuario
 */
@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BffController {

    private final BffService bffService;

    /**
     * GET /api/bff/dashboard?rol=EJECUTIVO
     * Retorna dashboard adaptado al rol
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> obtenerDashboard(
            @RequestParam(defaultValue = "EJECUTIVO") String rol) {
        DashboardDTO dashboard = bffService.generarDashboard(rol);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/bff/health
     * Verifica que el BFF está funcionando
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("BFF Cordillera funcionando correctamente");
    }
}