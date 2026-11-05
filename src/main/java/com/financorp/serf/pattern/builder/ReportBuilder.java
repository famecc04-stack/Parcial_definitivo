package com.financorp.serf.pattern.builder;

import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import com.financorp.serf.pattern.prototype.ReportTemplate;
import java.util.List;

/**
 * Patrón Builder: Interfaz constructora del reporte financiero.
 */
public interface ReportBuilder {
    void reset();
    void applyTemplate(ReportTemplate template);
    void buildSections(List<Sale> sales, List<Product> products);
    void buildTables(List<Sale> sales, List<Product> products);
    void buildSummary();
    FinancialReport getResult();
}
