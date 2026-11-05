package com.financorp.serf.model;

import com.financorp.serf.config.CorporateConfig;

/**
 * Modelo de Venta en Filial (Perú en PEN / Chile en USD).
 * Convierte automáticamente los ingresos a la moneda base: Euros (EUR).
 */
public class Sale {
    private String invoiceNumber;
    private String date;
    private String subsidiary; // "Perú" o "Chile"
    private String productSku;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalLocal;
    private String currency;   // "PEN" o "USD"
    private double totalEur;
    private String client;
    private String seller;
    private String paymentMethod; // "Efectivo", "Tarjeta", "Transferencia"

    public Sale(String invoiceNumber, String date, String subsidiary, String productSku,
                String productName, int quantity, double unitPrice, String client,
                String seller, String paymentMethod) {
        if (quantity <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        if (unitPrice <= 0) throw new IllegalArgumentException("El precio unitario debe ser positivo");
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.subsidiary = subsidiary;
        this.productSku = productSku;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalLocal = quantity * unitPrice;
        this.currency = subsidiary.equalsIgnoreCase("Chile") ? "USD" : "PEN";
        this.totalEur = CorporateConfig.getInstance().convertToEur(this.totalLocal, this.currency);
        this.client = client;
        this.seller = seller;
        this.paymentMethod = paymentMethod;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public String getDate() { return date; }
    public String getSubsidiary() { return subsidiary; }
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalLocal() { return totalLocal; }
    public String getCurrency() { return currency; }
    public double getTotalEur() { return totalEur; }
    public String getClient() { return client; }
    public String getSeller() { return seller; }
    public String getPaymentMethod() { return paymentMethod; }
}
