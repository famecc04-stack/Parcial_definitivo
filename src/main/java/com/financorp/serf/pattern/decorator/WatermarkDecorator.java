package com.financorp.serf.pattern.decorator;

import com.financorp.serf.pattern.builder.FinancialReport;

/**
 * Patrón Decorator Concreto: Agrega la marca de agua institucional CONFIDENCIAL.
 */
public class WatermarkDecorator extends ReportDecorator {

    public WatermarkDecorator(FinancialReport report) {
        super(report);
    }

    @Override
    public void decorate() {
        wrappedReport.setWatermark("CONFIDENCIAL - USO INTERNO EXCLUSIVO FINANCORP S.A.");
    }
}
