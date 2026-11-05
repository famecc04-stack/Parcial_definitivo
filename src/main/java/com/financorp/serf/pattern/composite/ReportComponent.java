package com.financorp.serf.pattern.composite;

import java.util.Map;

/**
 * Patrón Composite: Interfaz Component para la estructura arbórea del reporte.
 */
public interface ReportComponent {
    String getTitle();
    double getTotalEur();
    Map<String, Object> toMap();
}
