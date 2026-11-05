package com.financorp.serf.pattern.builder;

import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import com.financorp.serf.pattern.composite.*;
import com.financorp.serf.pattern.prototype.ReportTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Patrón Builder Concreto: Construye paso a paso el reporte financiero en Euros.
 */
public class StandardReportBuilder implements ReportBuilder {
    private FinancialReport report;

    public StandardReportBuilder() { this.reset(); }

    @Override
    public void reset() { this.report = new FinancialReport(); }

    @Override
    public void applyTemplate(ReportTemplate template) {
        report.setTitle(template.getTitle());
        report.setPeriod(template.getPeriod());
        report.setTargetAudience(template.getTargetAudience());
        report.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public void buildSections(List<Sale> sales, List<Product> products) {
        // 1. Sección Composite: Ingresos por Filial / País
        ReportSection countrySection = new ReportSection("1. Ingresos por Filial / País (EUR)");
        Map<String, Double> byCountry = new HashMap<>();
        for (Sale s : sales) {
            byCountry.put(s.getSubsidiary(), byCountry.getOrDefault(s.getSubsidiary(), 0.0) + s.getTotalEur());
        }
        for (Map.Entry<String, Double> entry : byCountry.entrySet()) {
            countrySection.add(new ReportItem("Filial " + entry.getKey(), entry.getValue(), "Ventas convertidas a EUR"));
        }
        report.addSection(countrySection);

        // 2. Sección Composite: Ingresos por Categoría de Producto
        ReportSection categorySection = new ReportSection("2. Ingresos por Categoría de Producto (EUR)");
        Map<String, Double> byCategory = new HashMap<>();
        for (Sale s : sales) {
            String cat = products.stream().filter(p -> p.getSku().equalsIgnoreCase(s.getProductSku()))
                    .map(Product::getCategory).findFirst().orElse("General");
            byCategory.put(cat, byCategory.getOrDefault(cat, 0.0) + s.getTotalEur());
        }
        for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
            categorySection.add(new ReportItem("Categoría: " + entry.getKey(), entry.getValue(), "Consolidado en EUR"));
        }
        report.addSection(categorySection);

        // 3. Sección Composite: Gastos de Importación (Shenzhen - China)
        ReportSection expenseSection = new ReportSection("3. Gastos de Importación (Shenzhen - China)");
        for (Product p : products) {
            double totalLotCostEur = p.getCostEur() * p.getStock();
            expenseSection.add(new ReportItem("Lote: " + p.getName(), totalLotCostEur, p.getStock() + " unids. a " + Math.round(p.getCostEur() * 100.0) / 100.0 + " EUR c/u"));
        }
        report.addSection(expenseSection);
    }

    @Override
    public void buildTables(List<Sale> sales, List<Product> products) {
        report.setConsolidatedSales(sales);
        report.setConsolidatedProducts(products);
    }

    @Override
    public void buildSummary() {
        double revenue = report.getSections().get(0).getTotalEur();
        double expenses = report.getSections().get(2).getTotalEur();
        report.setTotals(revenue, expenses);
    }

    @Override
    public FinancialReport getResult() { return this.report; }
}
