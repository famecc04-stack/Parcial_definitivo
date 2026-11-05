package com.financorp.serf.pattern.decorator;

import com.financorp.serf.pattern.builder.FinancialReport;

/**
 * Patrón Decorator: Clase base abstracta para agregar capas al reporte financiero.
 */
public abstract class ReportDecorator {
    protected final FinancialReport wrappedReport;

    public ReportDecorator(FinancialReport report) {
        this.wrappedReport = report;
    }

    public abstract void decorate();

    public FinancialReport getReport() {
        return wrappedReport;
    }
}
