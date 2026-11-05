package com.financorp.serf.pattern.facade;

import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import com.financorp.serf.pattern.builder.FinancialReport;
import com.financorp.serf.pattern.builder.ReportBuilder;
import com.financorp.serf.pattern.builder.StandardReportBuilder;
import com.financorp.serf.pattern.decorator.SignatureDecorator;
import com.financorp.serf.pattern.decorator.WatermarkDecorator;
import com.financorp.serf.pattern.prototype.ReportTemplate;
import com.financorp.serf.repository.DataStore;

import java.util.List;

/**
 * Patrón Facade: Proporciona un punto de entrada simple y unificado para ensamblar
 * y decorar reportes financieros utilizando Prototype, Builder, Composite y Decorator.
 */
public class ReportFacade {
    private final DataStore dataStore;

    public ReportFacade() {
        this.dataStore = DataStore.getInstance();
    }

    public FinancialReport generateReport(String templateType, boolean withWatermark, boolean withSignature) {
        // 1. Prototipo: Selecciona y clona la plantilla base
        ReportTemplate prototype = ReportTemplate.getAvailablePrototypes().getOrDefault(templateType,
                ReportTemplate.getAvailablePrototypes().get("MENSUAL"));
        ReportTemplate templateClone = prototype.cloneReport();

        // 2. Builder: Construcción paso a paso del reporte
        ReportBuilder builder = new StandardReportBuilder();
        builder.applyTemplate(templateClone);

        List<Sale> sales = dataStore.getAllSales();
        List<Product> products = dataStore.getAllProducts();

        // 3. Composite: Ensamblado de secciones jerárquicas
        builder.buildSections(sales, products);
        builder.buildTables(sales, products);
        builder.buildSummary();

        FinancialReport report = builder.getResult();

        // 4. Decorator: Aplicación dinámica de capas de seguridad visual
        if (withWatermark) {
            new WatermarkDecorator(report).decorate();
        }
        if (withSignature) {
            new SignatureDecorator(report).decorate();
        }

        return report;
    }
}
