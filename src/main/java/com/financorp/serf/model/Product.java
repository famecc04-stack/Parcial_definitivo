package com.financorp.serf.model;

import com.financorp.serf.config.CorporateConfig;

/**
 * Modelo de Producto Importado desde Shenzhen (China).
 * Aplica programación defensiva en constructor y mutadores.
 */
public class Product {
    private String sku;
    private String name;
    private String technicalDescription;
    private String category; // Laptop, Smartphone, Accesorio, Equipo de red
    private double costCny;
    private double costEur;
    private double suggestedPrice;
    private String supplier;
    private String date;
    private int stock;

    public Product(String sku, String name, String technicalDescription, String category,
                   double costCny, double suggestedPrice, String supplier, String date, int stock) {
        validateDefense(costCny, stock);
        this.sku = sku;
        this.name = name;
        this.technicalDescription = technicalDescription;
        this.category = category;
        this.costCny = costCny;
        this.costEur = CorporateConfig.getInstance().cnyToEur(costCny);
        this.suggestedPrice = suggestedPrice;
        this.supplier = supplier;
        this.date = date;
        this.stock = stock;
    }

    private void validateDefense(double costCny, int stock) {
        if (costCny < 0) {
            throw new IllegalArgumentException("Validación defensiva: El costo de importación no puede ser negativo (" + costCny + " CNY)");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Validación defensiva: El stock inicial no puede ser menor a cero (" + stock + ")");
        }
    }

    public synchronized void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Stock insuficiente para " + name + " (SKU: " + sku + "). Disponible: " + this.stock + ", Requerido: " + quantity);
        }
        this.stock -= quantity;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getTechnicalDescription() { return technicalDescription; }
    public String getCategory() { return category; }
    public double getCostCny() { return costCny; }
    public double getCostEur() { return costEur; }
    public double getSuggestedPrice() { return suggestedPrice; }
    public String getSupplier() { return supplier; }
    public String getDate() { return date; }
    public int getStock() { return stock; }
    public void setStock(int stock) { validateDefense(this.costCny, stock); this.stock = stock; }
}
