package com.financorp.serf.repository;

import com.financorp.serf.model.Product;
import com.financorp.serf.model.Sale;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Repositorio en memoria thread-safe con control atómico de inventario.
 */
public class DataStore {
    private static final DataStore instance = new DataStore();
    private final List<Product> products = new CopyOnWriteArrayList<>();
    private final List<Sale> sales = new CopyOnWriteArrayList<>();

    private DataStore() {
        seedInitialData();
    }

    public static DataStore getInstance() { return instance; }

    public synchronized void addProduct(Product product) {
        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(product.getSku())) {
                throw new IllegalArgumentException("Ya existe un producto con el SKU: " + product.getSku());
            }
        }
        products.add(product);
    }

    public Optional<Product> findProductBySku(String sku) {
        return products.stream().filter(p -> p.getSku().equalsIgnoreCase(sku)).findFirst();
    }

    // Lógica atómica: valida stock antes de registrar la venta
    public synchronized Sale registerSale(Sale sale) {
        Product product = findProductBySku(sale.getProductSku())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con SKU: " + sale.getProductSku()));

        // Descuenta atómicamente; si no hay stock lanza IllegalStateException
        product.reduceStock(sale.getQuantity());
        sales.add(sale);
        return sale;
    }

    public List<Product> getAllProducts() { return Collections.unmodifiableList(products); }
    public List<Sale> getAllSales() { return Collections.unmodifiableList(sales); }

    private void seedInitialData() {
        // Lotes importados desde Shenzhen (China)
        products.add(new Product("SKU-LAP-01", "Laptop ThinkEnterprise X1", "Intel Core i7 16GB RAM 512GB SSD", "Laptop", 4500.00, 1100.00, "Shenzhen Tech Ltd", "2026-10-15", 25));
        products.add(new Product("SKU-PHO-02", "Smartphone FinanPro 5G", "OLED 6.7'' 8GB RAM 256GB Dual SIM", "Smartphone", 2200.00, 520.00, "Shenzhen Mobile Corp", "2026-10-18", 40));
        products.add(new Product("SKU-NET-03", "Router Enterprise Gigabit AX3000", "Wi-Fi 6 4 Antenas Balanceador WAN", "Equipo de red", 550.00, 140.00, "Shenzhen NetCom", "2026-10-20", 30));
        products.add(new Product("SKU-ACC-04", "Docking Station USB-C Multiport", "Dual HDMI 4K PD 100W RJ45 Gigabit", "Accesorio", 180.00, 55.00, "Shenzhen Peripherals", "2026-10-22", 60));

        // Ventas iniciales en filiales
        sales.add(new Sale("B001-000412", "2026-10-25", "Perú", "SKU-LAP-01", "Laptop ThinkEnterprise X1", 3, 4200.00, "Banco Continental", "Carlos Mendoza", "Transferencia"));
        sales.add(new Sale("B001-000413", "2026-10-26", "Perú", "SKU-PHO-02", "Smartphone FinanPro 5G", 5, 2100.00, "Minera Las Bambas", "Lucía Paredes", "Tarjeta"));
        sales.add(new Sale("F002-000881", "2026-10-27", "Chile", "SKU-NET-03", "Router Enterprise Gigabit AX3000", 4, 150.00, "Telecom Santiago", "Andrés Morales", "Transferencia"));
        sales.add(new Sale("F002-000882", "2026-10-28", "Chile", "SKU-ACC-04", "Docking Station USB-C Multiport", 8, 60.00, "Inversiones del Pacífico", "Javiera Soto", "Efectivo"));
    }
}
