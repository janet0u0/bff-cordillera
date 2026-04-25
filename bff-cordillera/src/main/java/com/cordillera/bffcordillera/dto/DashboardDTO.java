package com.cordillera.bffcordillera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO del Dashboard
 * El BFF adapta este objeto según el rol del usuario.
 * Patrón Factory Method: cada rol recibe información diferente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private String tipoReporte;      // EJECUTIVO | ANALISTA | SUPERVISOR
    private String rol;
    private LocalDateTime generadoEn;
    private Boolean datosEnTiempoReal;
    private String mensaje;

    // KPIs básicos (todos los roles los ven)
    private Double ventasTotales;
    private Double metaVentas;
    private Double porcentajeCumplimiento;
    private String estadoGeneral;    // VERDE | AMARILLO | ROJO

    // Solo EJECUTIVO
    private Double rentabilidadNeta;

    // Solo ANALISTA y SUPERVISOR
    private Integer productosStockCritico;
    private Integer transaccionesHoy;
}