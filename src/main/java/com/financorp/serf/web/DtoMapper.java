package com.financorp.serf.web;

import com.financorp.serf.config.CorporateConfig;
import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import com.financorp.serf.pattern.builder.FinancialReport;
import com.financorp.serf.pattern.composite.ReportSection;

import java.util.*;

/**
 * Conversor de entidades de dominio a mapas serializables a JSON.
 */
public class DtoMapper {

    public static Map<String, Object> configToMap(CorporateConfig c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("penRate", c.getPenRate());
        map.put("cnyRate", c.getCnyRate());
        map.put("usdRate", c.getUsdRate());
        map.put("subsidiaries", c.getSubsidiaries());
        map.put("baseCurrency", "EUR");
        return map;
    }

    public static Map<String, Object> productToMap(Product p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("sku", p.getSku());
        map.put("name", p.getName());
        map.put("technicalDescription", p.getTechnicalDescription());
        map.put("category", p.getCategory());
        map.put("costCny", p.getCostCny());
        map.put("costEur", Math.round(p.getCostEur() * 100.0) / 100.0);
        map.put("suggestedPrice", p.getSuggestedPrice());
        map.put("supplier", p.getSupplier());
        map.put("date", p.getDate());
        map.put("stock", p.getStock());
        return map;
    }

    public static Map<String, Object> saleToMap(Sale s) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("invoiceNumber", s.getInvoiceNumber());
        map.put("date", s.getDate());
        map.put("subsidiary", s.getSubsidiary());
        map.put("productSku", s.getProductSku());
        map.put("productName", s.getProductName());
        map.put("quantity", s.getQuantity());
        map.put("unitPrice", s.getUnitPrice());
        map.put("totalLocal", Math.round(s.getTotalLocal() * 100.0) / 100.0);
        map.put("currency", s.getCurrency());
        map.put("totalEur", Math.round(s.getTotalEur() * 100.0) / 100.0);
        map.put("client", s.getClient());
        map.put("seller", s.getSeller());
        map.put("paymentMethod", s.getPaymentMethod());
        return map;
    }

    public static Map<String, Object> reportToMap(FinancialReport r) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("title", r.getTitle());
        map.put("period", r.getPeriod());
        map.put("generatedAt", r.getGeneratedAt());
        map.put("targetAudience", r.getTargetAudience());
        map.put("totalRevenueEur", Math.round(r.getTotalRevenueEur() * 100.0) / 100.0);
        map.put("totalExpenseEur", Math.round(r.getTotalExpenseEur() * 100.0) / 100.0);
        map.put("netProfitEur", Math.round(r.getNetProfitEur() * 100.0) / 100.0);
        map.put("watermark", r.getWatermark());
        map.put("digitalSignature", r.getDigitalSignature());

        List<Map<String, Object>> secMaps = new ArrayList<>();
        for (ReportSection sec : r.getSections()) {
            secMaps.add(sec.toMap());
        }
        map.put("sections", secMaps);
        return map;
    }
}
