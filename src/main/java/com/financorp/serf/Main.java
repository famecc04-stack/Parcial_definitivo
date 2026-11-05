package com.financorp.serf;

import com.financorp.serf.config.CorporateConfig;
import com.financorp.serf.pattern.builder.FinancialReport;
import com.financorp.serf.pattern.builder.ReportBuilder;
import com.financorp.serf.pattern.builder.StandardReportBuilder;
import com.financorp.serf.pattern.composite.ReportItem;
import com.financorp.serf.pattern.composite.ReportSection;
import com.financorp.serf.pattern.decorator.SignatureDecorator;
import com.financorp.serf.pattern.decorator.WatermarkDecorator;
import com.financorp.serf.pattern.facade.ReportFacade;
import com.financorp.serf.pattern.prototype.ReportTemplate;
import com.financorp.serf.repository.DataStore;

import java.util.Scanner;

/**
 * SERF - Menu Interactivo de Demostracion de Patrones GoF
 * FinanCorp S.A. | Autoras: Fabiola Cadillo / Estefania Orellana
 */
public class Main {

    static final String LINE  = "=".repeat(60);
    static final String DLINE = "-".repeat(60);
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();
            System.out.println();
            switch (opcion) {
                case "1": demoSingleton();   break;
                case "2": demoPrototype();   break;
                case "3": demoBuilder();     break;
                case "4": demoComposite();   break;
                case "5": demoDecorator();   break;
                case "6": demoFacade();      break;
                case "7": demoConversion();  break;
                case "8": demoTodos();       break;
                case "0": continuar = false; break;
                default:
                    System.out.println("  Opcion no valida. Intente de nuevo.");
            }
            if (continuar && !opcion.equals("8")) {
                System.out.println("\n  Presiona ENTER para volver al menu...");
                scanner.nextLine();
            }
        }
        System.out.println(LINE);
        System.out.println("  Sesion SERF finalizada. FinanCorp S.A.");
        System.out.println(LINE);
    }

    // ── MENU ─────────────────────────────────────────────────
    static void mostrarMenu() {
        System.out.println("\n" + LINE);
        System.out.println("  SERF - FinanCorp S.A. | Demo de Patrones GoF");
        System.out.println("  Autoras: Fabiola Cadillo / Estefania Orellana");
        System.out.println(LINE);
        System.out.println("  [1] Patron Singleton  - Configuracion Global");
        System.out.println("  [2] Patron Prototype  - Clonacion de Plantillas");
        System.out.println("  [3] Patron Builder    - Construccion de Reporte");
        System.out.println("  [4] Patron Composite  - Secciones Jerarquicas");
        System.out.println("  [5] Patron Decorator  - Watermark y Firma Digital");
        System.out.println("  [6] Patron Facade     - Reporte en un solo paso");
        System.out.println("  [7] Conversion de Monedas (EUR, PEN, CNY, USD)");
        System.out.println("  [8] Ejecutar TODOS los patrones");
        System.out.println("  [0] Salir");
        System.out.println(DLINE);
        System.out.print("  Selecciona una opcion: ");
    }

    // ── 1. SINGLETON ─────────────────────────────────────────
    static void demoSingleton() {
        System.out.println(LINE);
        System.out.println("  [PATRON 1] SINGLETON - CorporateConfig");
        System.out.println(LINE);
        CorporateConfig cfg1 = CorporateConfig.getInstance();
        CorporateConfig cfg2 = CorporateConfig.getInstance();
        System.out.println("  Instancia 1 == Instancia 2: " + (cfg1 == cfg2));
        System.out.println("  >> Solo existe UNA instancia en toda la aplicacion.");
        System.out.println();
        System.out.println("  Tasas de cambio oficiales (moneda base: EUR):");
        System.out.println("    1 EUR = " + cfg1.getPenRate() + " PEN  (Filial Peru)");
        System.out.println("    1 EUR = " + cfg1.getCnyRate() + " CNY  (Shenzhen, China)");
        System.out.println("    1 EUR = " + cfg1.getUsdRate() + " USD  (Filial Chile)");
        System.out.println("  Filiales: " + cfg1.getSubsidiaries());
    }

    // ── 2. PROTOTYPE ─────────────────────────────────────────
    static void demoPrototype() {
        System.out.println(LINE);
        System.out.println("  [PATRON 2] PROTOTYPE - ReportTemplate");
        System.out.println(LINE);
        ReportTemplate original = new ReportTemplate(
                "MENSUAL", "Reporte Mensual de Ingresos",
                "Noviembre 2026", "Consolidacion mensual de ventas.", "Gerencia");
        ReportTemplate clon = original.cloneReport();
        clon.setTitle("Reporte Trimestral (Clonado)");
        clon.setPeriod("Q4 2026");
        System.out.println("  Plantilla ORIGINAL:");
        System.out.println("    Titulo  : " + original.getTitle());
        System.out.println("    Periodo : " + original.getPeriod());
        System.out.println();
        System.out.println("  Plantilla CLONADA (modificada sin afectar original):");
        System.out.println("    Titulo  : " + clon.getTitle());
        System.out.println("    Periodo : " + clon.getPeriod());
        System.out.println();
        System.out.println("  Son objetos distintos: " + (original != clon));
        System.out.println("  >> El clon no afecta al original. Patron Prototype OK.");
    }

    // ── 3. BUILDER ───────────────────────────────────────────
    static void demoBuilder() {
        System.out.println(LINE);
        System.out.println("  [PATRON 3] BUILDER - StandardReportBuilder");
        System.out.println(LINE);
        DataStore store = DataStore.getInstance();
        ReportTemplate plantilla = new ReportTemplate(
                "MENSUAL", "Reporte Mensual de Ingresos",
                "Noviembre 2026", "Consolidacion mensual.", "Gerencia");
        ReportBuilder builder = new StandardReportBuilder();
        System.out.println("  Paso 1: applyTemplate()  -> Aplicando plantilla...");
        builder.applyTemplate(plantilla);
        System.out.println("  Paso 2: buildSections()  -> Construyendo secciones...");
        builder.buildSections(store.getAllSales(), store.getAllProducts());
        System.out.println("  Paso 3: buildTables()    -> Construyendo tablas...");
        builder.buildTables(store.getAllSales(), store.getAllProducts());
        System.out.println("  Paso 4: buildSummary()   -> Calculando resumen...");
        builder.buildSummary();
        FinancialReport report = builder.getResult();
        System.out.println();
        System.out.println("  REPORTE CONSTRUIDO:");
        System.out.println("    Titulo    : " + report.getTitle());
        System.out.println("    Periodo   : " + report.getPeriod());
        System.out.println("    Audiencia : " + report.getTargetAudience());
        System.out.println("    Ingresos  : " + String.format("%.2f EUR", report.getTotalRevenueEur()));
        System.out.println("    Gastos    : " + String.format("%.2f EUR", report.getTotalExpenseEur()));
        System.out.println("    Ganancia  : " + String.format("%.2f EUR", report.getNetProfitEur()));
    }

    // ── 4. COMPOSITE ─────────────────────────────────────────
    static void demoComposite() {
        System.out.println(LINE);
        System.out.println("  [PATRON 4] COMPOSITE - ReportSection / ReportItem");
        System.out.println(LINE);
        CorporateConfig cfg = CorporateConfig.getInstance();
        ReportSection seccion = new ReportSection("Ventas Consolidadas EUR");
        seccion.add(new ReportItem("Laptop x3  - Filial Peru",  cfg.penToEur(3 * 4200.00), "PEN -> EUR"));
        seccion.add(new ReportItem("Router x4  - Filial Chile", cfg.usdToEur(4 * 150.00),  "USD -> EUR"));
        seccion.add(new ReportItem("Smartphone x5 - Filial Peru", cfg.penToEur(5 * 2100.00), "PEN -> EUR"));
        System.out.println("  Estructura jerarquica:");
        System.out.println("  [Seccion] " + seccion.getTitle());
        for (var child : seccion.getChildren()) {
            System.out.println("    [Item] " + child.getTitle()
                    + "  =>  " + String.format("%.2f EUR", child.getTotalEur()));
        }
        System.out.println();
        System.out.println("  TOTAL SECCION: " + String.format("%.2f EUR", seccion.getTotalEur()));
        System.out.println("  >> El Composite suma automaticamente todos los items.");
    }

    // ── 5. DECORATOR ─────────────────────────────────────────
    static void demoDecorator() {
        System.out.println(LINE);
        System.out.println("  [PATRON 5] DECORATOR - Watermark + Firma Digital");
        System.out.println(LINE);
        DataStore store = DataStore.getInstance();
        ReportTemplate plantilla = new ReportTemplate(
                "MENSUAL", "Reporte Base", "Nov 2026", "Demo", "Gerencia");
        ReportBuilder builder = new StandardReportBuilder();
        builder.applyTemplate(plantilla);
        builder.buildSections(store.getAllSales(), store.getAllProducts());
        builder.buildTables(store.getAllSales(), store.getAllProducts());
        builder.buildSummary();
        FinancialReport report = builder.getResult();

        System.out.println("  Reporte SIN decoradores:");
        System.out.println("    Watermark : (vacio)");
        System.out.println("    Firma     : (ninguna)");

        new WatermarkDecorator(report).decorate();
        System.out.println();
        System.out.println("  Despues de WatermarkDecorator:");
        System.out.println("    Watermark : " + report.getWatermark());

        new SignatureDecorator(report).decorate();
        System.out.println();
        System.out.println("  Despues de SignatureDecorator:");
        System.out.println("    Firma     : " + report.getDigitalSignature());
        System.out.println();
        System.out.println("  >> Decoradores apilados sin modificar la clase original.");
    }

    // ── 6. FACADE ────────────────────────────────────────────
    static void demoFacade() {
        System.out.println(LINE);
        System.out.println("  [PATRON 6] FACADE - ReportFacade");
        System.out.println(LINE);
        System.out.println("  Internamente usa: Prototype + Builder + Composite + Decorator");
        System.out.println("  El profesor solo llama UN metodo:");
        System.out.println("    facade.generateReport(\"TRIMESTRAL\", true, true)");
        System.out.println();
        ReportFacade facade = new ReportFacade();
        FinancialReport rep = facade.generateReport("TRIMESTRAL", true, true);
        System.out.println("  RESULTADO:");
        System.out.println("    Titulo    : " + rep.getTitle());
        System.out.println("    Periodo   : " + rep.getPeriod());
        System.out.println("    Ganancia  : " + String.format("%.2f EUR", rep.getNetProfitEur()));
        System.out.println("    Watermark : " + rep.getWatermark());
        System.out.println("    Firmado   : SI - " + rep.getDigitalSignature().get("authors"));
        System.out.println();
        System.out.println("  >> Facade oculta la complejidad. Un solo punto de entrada.");
    }

    // ── 7. CONVERSION DE MONEDAS ─────────────────────────────
    static void demoConversion() {
        System.out.println(LINE);
        System.out.println("  [EXTRA] CONVERSION DE MONEDAS - CorporateConfig");
        System.out.println(LINE);
        CorporateConfig cfg = CorporateConfig.getInstance();
        System.out.println("  Tasas oficiales FinanCorp S.A. (base: EUR):");
        System.out.println("    1 EUR = " + cfg.getPenRate() + " PEN");
        System.out.println("    1 EUR = " + cfg.getCnyRate() + " CNY");
        System.out.println("    1 EUR = " + cfg.getUsdRate() + " USD");
        System.out.println();
        System.out.println("  Ejemplos de conversion a EUR:");
        System.out.printf("    100.00 PEN  =>  %.4f EUR%n", cfg.penToEur(100.00));
        System.out.printf("    100.00 CNY  =>  %.4f EUR%n", cfg.cnyToEur(100.00));
        System.out.printf("    100.00 USD  =>  %.4f EUR%n", cfg.usdToEur(100.00));
        System.out.println();
        System.out.println("  Conversion de ventas reales:");
        System.out.printf("    Laptop Peru  3 x 4200 PEN  =>  %.2f EUR%n", cfg.penToEur(3 * 4200.00));
        System.out.printf("    Router Chile 4 x 150  USD  =>  %.2f EUR%n", cfg.usdToEur(4 * 150.00));
        System.out.printf("    Lote Shenzhen 320 CNY/u    =>  %.2f EUR%n", cfg.cnyToEur(320.00));
        System.out.println();
        System.out.print("  Ingresa un monto para convertir (ej: 500): ");
        try {
            double monto = Double.parseDouble(scanner.nextLine().trim());
            System.out.println();
            System.out.printf("    %.2f PEN  =>  %.4f EUR%n", monto, cfg.penToEur(monto));
            System.out.printf("    %.2f CNY  =>  %.4f EUR%n", monto, cfg.cnyToEur(monto));
            System.out.printf("    %.2f USD  =>  %.4f EUR%n", monto, cfg.usdToEur(monto));
        } catch (Exception e) {
            System.out.println("  (Monto no valido, se omite la conversion personalizada)");
        }
    }

    // ── 8. TODOS ─────────────────────────────────────────────
    static void demoTodos() {
        demoSingleton();  pause();
        demoPrototype();  pause();
        demoBuilder();    pause();
        demoComposite();  pause();
        demoDecorator();  pause();
        demoFacade();
        System.out.println("\n" + LINE);
        System.out.println("  TODOS LOS PATRONES GoF EJECUTADOS CORRECTAMENTE");
        System.out.println("  [1] Singleton  [2] Prototype  [3] Builder");
        System.out.println("  [4] Composite  [5] Decorator  [6] Facade");
        System.out.println(LINE);
    }

    static void pause() {
        System.out.println("\n  " + DLINE);
    }
}
