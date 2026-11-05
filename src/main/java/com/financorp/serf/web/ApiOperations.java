package com.financorp.serf.web;

import com.financorp.serf.config.CorporateConfig;
import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import com.financorp.serf.pattern.builder.FinancialReport;
import com.financorp.serf.pattern.facade.ReportFacade;
import com.financorp.serf.repository.DataStore;
import java.util.Map;

/**
 * Operaciones de negocio para la API REST de SERF.
 */
public class ApiOperations {
    private static final DataStore dataStore = DataStore.getInstance();
    private static final ReportFacade reportFacade = new ReportFacade();

    public static String updateConfig(String bodyJson) {
        Map<String, String> b = JsonUtil.parseSimpleJson(bodyJson);
        double pen = Double.parseDouble(b.getOrDefault("penRate", "4.13"));
        double cny = Double.parseDouble(b.getOrDefault("cnyRate", "7.82"));
        double usd = Double.parseDouble(b.getOrDefault("usdRate", "1.08"));
        CorporateConfig.getInstance().updateRates(pen, cny, usd);
        return JsonUtil.successJson("Tasas de cambio actualizadas correctamente");
    }

    public static String createProduct(String bodyJson) {
        Map<String, String> b = JsonUtil.parseSimpleJson(bodyJson);
        Product p = new Product(b.get("sku"), b.get("name"), b.get("technicalDescription"), b.get("category"),
                Double.parseDouble(b.getOrDefault("costCny", "0")), Double.parseDouble(b.getOrDefault("suggestedPrice", "0")),
                b.get("supplier"), b.get("date"), Integer.parseInt(b.getOrDefault("stock", "0")));
        dataStore.addProduct(p);
        return JsonUtil.toJson(DtoMapper.productToMap(p));
    }

    public static String createSale(String bodyJson) {
        Map<String, String> b = JsonUtil.parseSimpleJson(bodyJson);
        Sale s = new Sale(b.get("invoiceNumber"), b.get("date"), b.get("subsidiary"), b.get("productSku"),
                b.get("productName"), Integer.parseInt(b.getOrDefault("quantity", "1")),
                Double.parseDouble(b.getOrDefault("unitPrice", "0")), b.get("client"), b.get("seller"), b.get("paymentMethod"));
        Sale registered = dataStore.registerSale(s);
        return JsonUtil.toJson(DtoMapper.saleToMap(registered));
    }

    public static String generateReport(String bodyJson) {
        Map<String, String> b = JsonUtil.parseSimpleJson(bodyJson);
        String template = b.getOrDefault("templateType", "MENSUAL");
        boolean watermark = Boolean.parseBoolean(b.getOrDefault("withWatermark", "true"));
        boolean signature = Boolean.parseBoolean(b.getOrDefault("withSignature", "true"));
        FinancialReport report = reportFacade.generateReport(template, watermark, signature);
        return JsonUtil.toJson(DtoMapper.reportToMap(report));
    }
}
