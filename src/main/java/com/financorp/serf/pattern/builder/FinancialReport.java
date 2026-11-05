package com.financorp.serf.pattern.builder;

import com.financorp.serf.pattern.composite.ReportSection;
import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import java.util.*;

/**
 * Producto resultante del Patrón Builder: Reporte Financiero ensamblado.
 */
public class FinancialReport {
    private String title;
    private String period;
    private String generatedAt;
    private String targetAudience;
    private final List<ReportSection> sections = new ArrayList<>();
    private final List<Sale> consolidatedSales = new ArrayList<>();
    private final List<Product> consolidatedProducts = new ArrayList<>();
    private double totalRevenueEur;
    private double totalExpenseEur;
    private double netProfitEur;
    private String watermark = "";
    private Map<String, String> digitalSignature = null;

    public void addSection(ReportSection section) { this.sections.add(section); }
    public void setTitle(String title) { this.title = title; }
    public void setPeriod(String period) { this.period = period; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    public void setConsolidatedSales(List<Sale> sales) { this.consolidatedSales.addAll(sales); }
    public void setConsolidatedProducts(List<Product> products) { this.consolidatedProducts.addAll(products); }
    public void setTotals(double rev, double exp) {
        this.totalRevenueEur = rev;
        this.totalExpenseEur = exp;
        this.netProfitEur = rev - exp;
    }
    public void setWatermark(String watermark) { this.watermark = watermark; }
    public void setDigitalSignature(Map<String, String> sig) { this.digitalSignature = sig; }

    public String getTitle() { return title; }
    public String getPeriod() { return period; }
    public String getGeneratedAt() { return generatedAt; }
    public String getTargetAudience() { return targetAudience; }
    public List<ReportSection> getSections() { return sections; }
    public List<Sale> getConsolidatedSales() { return consolidatedSales; }
    public List<Product> getConsolidatedProducts() { return consolidatedProducts; }
    public double getTotalRevenueEur() { return totalRevenueEur; }
    public double getTotalExpenseEur() { return totalExpenseEur; }
    public double getNetProfitEur() { return netProfitEur; }
    public String getWatermark() { return watermark; }
    public Map<String, String> getDigitalSignature() { return digitalSignature; }
}
