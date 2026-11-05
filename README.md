# SERF &mdash; Sistema Empresarial de Gestión de Reportes Financieros
### Multinacional FinanCorp S.A. &bull; Caso Práctico Universitario

Aplicación web corporativa B2B desarrollada **100% en lenguaje Java** (backend y servidor embebido nativo) con frontend moderno en HTML5, CSS corporativo sólido y JavaScript vanilla.

---

## 🏛️ Directrices Técnicas y Arquitectura

1. **Principio KISS (Keep It Simple, Stupid) y Código Humano**:
   - Estructura limpia y directa sin sobreingeniería de frameworks pesados.
   - **Menos de 100 líneas por archivo**: Cada clase Java se enfoca en una única responsabilidad (SRP), resultando en código altamente legible, didáctico y fácil de sustentar ante el docente evaluador.
2. **Paleta de Colores Corporativos B2B**:
   - Azul marino institucional: `#1A365D`
   - Azul slate: `#2B6CB0`
   - Fondo gris claro: `#F8FAFC`
   - Tarjetas blancas: `#FFFFFF` con bordes delgados `#E2E8F0`
3. **Moneda Base Obligatoria**:
   - **Euros (EUR)**: Todas las operaciones comerciales originadas en las filiales (Perú en PEN, Chile en USD) e importaciones desde China (Shenzhen en CNY) se consolidan unificadamente en Euros.

---

## 🧩 Mapeo de Patrones de Diseño (GoF)

| Patrón GoF | Clase(s) / Paquete | Rol en el Sistema SERF |
| :--- | :--- | :--- |
| **Singleton** | `com.financorp.serf.config.CorporateConfig` | Centraliza en una única instancia global thread-safe las tasas de conversión oficiales (`1 EUR = 4.13 PEN \| 1 EUR = 7.82 CNY \| 1 EUR = 1.08 USD`) y las filiales registradas. |
| **Prototype** | `ReportPrototype`, `ReportTemplate` | Permite clonar plantillas de reportes contables (`MENSUAL`, `TRIMESTRAL`, `ANUAL`) sin alterar las plantillas base maestras. |
| **Builder** | `ReportBuilder`, `StandardReportBuilder`, `FinancialReport` | Ensambla paso a paso el reporte contable: cabecera, estructura Composite, tablas consolidadas de ventas y balance resumido. |
| **Composite** | `ReportComponent`, `ReportSection`, `ReportItem` | Modela la jerarquía contable en forma de árbol con cálculo recursivo de subtotales en Euros: <br>1. Ingresos por Filial / País<br>2. Ingresos por Categoría de Producto<br>3. Gastos de Importación (Shenzhen). |
| **Decorator** | `ReportDecorator`, `WatermarkDecorator`, `SignatureDecorator` | Agrega capas dinámicas de seguridad visual: marca de agua institucional `CONFIDENCIAL` y tarjeta de Firma Digital con datos de las autoras. |
| **Facade** | `com.financorp.serf.pattern.facade.ReportFacade` | Proporciona el método unificado `generateReport(...)` que orquesta la selección de Prototype, ensamble con Builder, árbol Composite y aplicación de Decorators. |

---

## 📦 Módulos del Sistema

### 1. Barra Superior Global & Configuración Corporativa (Singleton)
- Widget visible permanente en el Header que exhibe las tasas oficiales frente a 1 Euro:
  `1 EUR = 4.13 PEN | 1 EUR = 7.82 CNY | 1 EUR = 1.08 USD`.
- Módulo administrativo para actualizar las tasas de cambio y consultar las filiales activas (`Filial Perú`, `Filial Chile`, `Sede Central Europa`).

### 2. Módulo 1: Registro de Productos Importados (Shenzhen - China)
- Formulario de alta para lotes comerciales: Código SKU, Nombre, Descripción técnica, Categoría (`Laptop`, `Smartphone`, `Accesorio`, `Equipo de red`), Costo en Yuanes (CNY), **conversión en vivo a Euros (EUR)**, Precio sugerido, Proveedor, Fecha y Stock inicial.
- **Validación defensiva obligatoria**: Lanza `IllegalArgumentException` ante costos negativos o inventario menor a cero, impidiendo el registro y emitiendo una alerta visual.

### 3. Módulo 2: Gestión de Ventas en Filiales
- Formulario de emisión de facturas para filiales: Perú (en PEN) o Chile (en USD), con cliente, vendedor, método de pago (`Efectivo`, `Tarjeta`, `Transferencia`).
- **Lógica de inventario atómica**: Verifica en tiempo real que exista stock suficiente antes de confirmar la venta, descontándolo automáticamente y lanzando un `IllegalStateException` si se intenta vender más de lo disponible.
- Conversión automática en tiempo real de la venta local a Euros (EUR).

### 4. Módulo 3: Generación de Reportes Financieros
- Selector de plantillas contables base reutilizables (**Prototype**).
- Botón unificado de generación (**Facade**).
- Desglose jerárquico por secciones (**Composite**).
- Opciones de seguridad visual (**Decorator**):
  - Marca de agua institucional `"CONFIDENCIAL - USO INTERNO EXCLUSIVO FINANCORP S.A."`.
  - Tarjeta de Firma Digital con autoras oficiales: **Fabiola Cadillo / Estefanía Orellana**, rol de auditoras y sello criptográfico SHA-256.

---

## 🚀 Guía de Instalación y Ejecución

### Requisitos Previos
- **Java JDK 17 o superior** (OpenJDK, Oracle JDK o JetBrains Runtime).

### Opción A: Ejecución Directa en Windows (Recomendada)
Hacer doble clic en `run.bat` o ejecutar desde terminal PowerShell:
```powershell
.\run.bat
```

### Opción B: Ejecución con Maven
```bash
mvn compile exec:java
```

Abra el navegador en:
👉 **`http://localhost:8080`**

---

## 👩‍💻 Autoría del Proyecto
- **Fabiola Cadillo** &bull; Auditora Financiera Senior &bull; FinanCorp S.A.
- **Estefanía Orellana** &bull; Auditora Financiera Senior &bull; FinanCorp S.A.
