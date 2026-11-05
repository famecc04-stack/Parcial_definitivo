package com.financorp.serf.pattern.prototype;

import java.util.*;

/**
 * Patrón Prototype: Plantilla concreta de reporte financiero que puede clonarse.
 */
public class ReportTemplate implements ReportPrototype {
    private String id;
    private String title;
    private String period;
    private String description;
    private String targetAudience;

    public ReportTemplate(String id, String title, String period, String description, String targetAudience) {
        this.id = id;
        this.title = title;
        this.period = period;
        this.description = description;
        this.targetAudience = targetAudience;
    }

    @Override
    public ReportTemplate cloneReport() {
        try {
            return (ReportTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            return new ReportTemplate(this.id, this.title, this.period, this.description, this.targetAudience);
        }
    }

    // Registro estático de prototipos disponibles para FinanCorp S.A.
    public static Map<String, ReportTemplate> getAvailablePrototypes() {
        Map<String, ReportTemplate> map = new LinkedHashMap<>();
        map.put("MENSUAL", new ReportTemplate("MENSUAL", "Reporte Mensual de Ingresos", "Noviembre 2026",
                "Consolidación mensual de ventas por filiales operativas.", "Gerencia de Operaciones"));
        map.put("TRIMESTRAL", new ReportTemplate("TRIMESTRAL", "Reporte Trimestral de Gastos e Ingresos", "Q4 2026",
                "Balance de compras internacionales vs facturación en filiales.", "Comité de Finanzas"));
        map.put("ANUAL", new ReportTemplate("ANUAL", "Reporte Anual Consolidado", "Ejercicio Fiscal 2026",
                "Auditoría global corporativa FinanCorp S.A. en Euros.", "Directorio Ejecutivo"));
        return map;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public String getTargetAudience() { return targetAudience; }
}
