package com.financorp.serf.config;

import java.util.Arrays;
import java.util.List;

/**
 * Patrón Singleton: Configuración Corporativa Global de FinanCorp S.A.
 * Centraliza las tasas de cambio oficiales frente a la moneda base: Euro (EUR).
 */
public class CorporateConfig {
    private static volatile CorporateConfig instance;

    // Tasas oficiales frente a 1 EUR: 1 EUR = 4.13 PEN | 1 EUR = 7.82 CNY | 1 EUR = 1.08 USD
    private double penRate; // Soles peruanos
    private double cnyRate; // Yuanes chinos (Shenzhen)
    private double usdRate; // Dólares estadounidenses (Chile / Global)
    private final List<String> subsidiaries;

    private CorporateConfig() {
        this.penRate = 4.13;
        this.cnyRate = 7.82;
        this.usdRate = 1.08;
        this.subsidiaries = Arrays.asList("Filial Perú (PEN)", "Filial Chile (USD)", "Sede Europa (EUR)");
    }

    public static CorporateConfig getInstance() {
        if (instance == null) {
            synchronized (CorporateConfig.class) {
                if (instance == null) {
                    instance = new CorporateConfig();
                }
            }
        }
        return instance;
    }

    // Conversiones a la moneda base: Euros (EUR)
    public double cnyToEur(double amountCny) {
        return amountCny / this.cnyRate;
    }

    public double penToEur(double amountPen) {
        return amountPen / this.penRate;
    }

    public double usdToEur(double amountUsd) {
        return amountUsd / this.usdRate;
    }

    public double convertToEur(double amount, String currency) {
        if (currency == null) return amount;
        switch (currency.toUpperCase().trim()) {
            case "CNY": return cnyToEur(amount);
            case "PEN": return penToEur(amount);
            case "USD": return usdToEur(amount);
            case "EUR": return amount;
            default: return amount;
        }
    }

    public synchronized void updateRates(double pen, double cny, double usd) {
        if (pen <= 0 || cny <= 0 || usd <= 0) {
            throw new IllegalArgumentException("Las tasas de cambio deben ser mayores a cero.");
        }
        this.penRate = pen;
        this.cnyRate = cny;
        this.usdRate = usd;
    }

    public double getPenRate() { return penRate; }
    public double getCnyRate() { return cnyRate; }
    public double getUsdRate() { return usdRate; }
    public List<String> getSubsidiaries() { return subsidiaries; }
}
