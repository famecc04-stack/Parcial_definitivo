# 📘 Guía Completa de Diagramas UML en Draw.io (diagrams.net)
**Proyecto:** SERF — Sistema Empresarial de Gestión de Reportes Financieros  
**Empresa:** FinanCorp S.A.  
**Autoras / Estudiantes:** Fabiola Cadillo & Estefanía Orellana (`vv45092612@zegel.pe`)  

---

## 🎯 1. ¿Qué archivos tienes listos en la carpeta `diagramas_uml`?

| Archivo | Tipo | Descripción |
| :--- | :--- | :--- |
| **`SERF_FinanCorp_Diagramas_UML.drawio`** | ⭐ **Maestro Draw.io** | Archivo multi-pestaña con los 3 diagramas completos listos para editar en draw.io. |
| `1_Diagrama_Clases_GoF.drawio` | Draw.io individual | Diagrama de clases con los 6 patrones GoF. |
| `2_Diagrama_Casos_de_Uso.drawio` | Draw.io individual | Diagrama de casos de uso funcionales y actores. |
| `3_Diagrama_Secuencia_Reportes.drawio` | Draw.io individual | Diagrama de secuencia del flujo de generación. |
| `1_Diagrama_Clases_GoF.jpg` / `.svg` | Imagen Alta Resolución | Vista previa instantánea para Word, PowerPoint o PDF. |
| `2_Diagrama_Casos_de_Uso.jpg` / `.svg` | Imagen Alta Resolución | Casos de uso listo para el informe académico. |
| `3_Diagrama_Secuencia_Reportes.jpg` / `.svg`| Imagen Alta Resolución | Diagrama de secuencia listo para exposición. |
| `PlantUML_*.puml` | Código PlantUML | Código fuente estándar para importar en draw.io con 1 clic. |

---

## 🚀 2. ¿Cómo abrir los diagramas en Draw.io (en 5 segundos)?

### Método A (El más fácil - Arrastrar y Soltar):
1. Entra en tu navegador a: **[https://app.diagrams.net](https://app.diagrams.net)** (draw.io).
2. Si te sale una ventana de bienvenida, haz clic en **"Abrir diagrama existente"** (o "Open Existing Diagram").
3. Selecciona el archivo:
   📂 `c:\Users\HP\Downloads\Serf_Estudio_final\diagramas_uml\SERF_FinanCorp_Diagramas_UML.drawio`
   *(O simplemente arrastra el archivo desde tu explorador de Windows directo a la ventana de draw.io)*.
4. **¡Listo!** Verás en la parte inferior **3 pestañas organizadas**:
   - 📑 **Pestaña 1:** `1. Diagrama de Clases (Patrones GoF)`
   - 📑 **Pestaña 2:** `2. Diagrama de Casos de Uso`
   - 📑 **Pestaña 3:** `3. Diagrama de Secuencia (Generación Reporte)`

### Método B (Insertar código PlantUML):
1. En draw.io, ve al menú superior: **Organizar (Arrange)** ➔ **Insertar (Insert)** ➔ **Avanzado (Advanced)** ➔ **PlantUML**.
2. Copia y pega el contenido del archivo `PlantUML_Clases_GoF.puml`.
3. Haz clic en **Insertar**. Draw.io generará el diagrama de inmediato.

---

## 🎓 3. Guía de Sustentación ante el Docente: Los 6 Patrones GoF

Cuando el docente les pida explicar la arquitectura del sistema, usen esta estructura clara:

### 1️⃣ Patrón Singleton (`CorporateConfig.java`)
* **Propósito:** Garantizar que exista una **única instancia** en toda la aplicación con las tasas de cambio corporativas (`1 EUR = 4.13 PEN`, `7.82 CNY`, `1.08 USD`) y filiales activas.
* **Justificación:** Si dos filiales modificaran tasas diferentes en instancias separadas, los balances contables en Euros tendrían inconsistencias graves. El constructor privado y `getInstance()` aseguran un punto de acceso global.

### 2️⃣ Patrón Prototype (`ReportPrototype.java`, `ReportTemplate.java`)
* **Propósito:** Permitir la **clonación dinámica** de estructuras de reportes financieros predefinidos (Mensual, Trimestral Q4, Anual Consolidado).
* **Justificación:** En lugar de construir una plantilla desde cero en cada petición, `cloneReport()` genera una copia exacta de la plantilla base en milisegundos, permitiendo su parametrización sin alterar la plantilla maestra.

### 3️⃣ Patrón Builder (`ReportBuilder.java`, `StandardReportBuilder.java`, `FinancialReport.java`)
* **Propósito:** Separar la construcción de un objeto complejo (`FinancialReport`) de su representación final, permitiendo ensamblarlo paso a paso.
* **Justificación:** Un reporte financiero no se crea en una sola línea; requiere ensamblar primero la cabecera (`buildHeader`), luego consolidar los datos de ventas e inventarios (`buildConsolidatedData`) y finalmente generar el resumen ejecutivo (`buildExecutiveSummary`).

### 4️⃣ Patrón Composite (`ReportComponent.java`, `ReportSection.java`, `ReportItem.java`)
* **Propósito:** Tratar de manera uniforme objetos individuales (líneas/ítems contables) y composiciones de objetos (secciones, subsecciones y categorías).
* **Justificación:** El reporte financiero se modela como un **árbol jerárquico**:
  - `ReportSection` ("Ingresos por Filial", "Gastos Shenzhen", "Ingresos por Categoría") contiene hijos que pueden ser otras secciones o ítems individuales.
  - Al invocar `getTotalEur()`, la sección suma recursivamente todos sus componentes hijos sin necesidad de conocer su tipo concreto.

### 5️⃣ Patrón Decorator (`ReportDecorator.java`, `WatermarkDecorator.java`, `SignatureDecorator.java`)
* **Propósito:** Agregar responsabilidades adicionales de seguridad y auditoría a un reporte de forma dinámica y sin modificar su código interno.
* **Justificación:** Cumple el principio **Open/Closed** (abierto a extensión, cerrado a modificación):
  - `WatermarkDecorator`: Envuelve el reporte para inyectar la marca de agua legal `"CONFIDENCIAL - USO INTERNO EXCLUSIVO"`.
  - `SignatureDecorator`: Inyecta la tarjeta de Firma Digital certificada de las autoras (**Fabiola Cadillo / Estefanía Orellana**) con su sello criptográfico SHA-256.

### 6️⃣ Patrón Facade (`ReportFacade.java`)
* **Propósito:** Proveer una **interfaz simplificada y unificada** que oculte la complejidad del subsistema contable.
* **Justificación:** El controlador web y la interfaz no necesitan saber cómo interactúan el DataStore, el Singleton, el Prototype, el Builder, el Composite ni los Decorators. Simplemente ejecutan:
  `reportFacade.generateReport("trimestral", true, true);`
  y la Fachada orquesta toda la colaboración interna.

---

## 🛡️ 4. Programación Defensiva y Control Atómico
* **Módulo 1 (Importaciones Shenzhen):** Validación defensiva con `IllegalArgumentException` si el costo o el stock ingresado es menor a 0.
* **Módulo 2 (Ventas en Filiales):** Validación atómica con `IllegalStateException` si la cantidad vendida excede el stock disponible en almacén, previniendo quiebres de inventario concurrentes.
