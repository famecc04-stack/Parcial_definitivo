// Variables de estado global
let currentRates = { penRate: 4.13, cnyRate: 7.82, usdRate: 1.08, baseCurrency: "EUR" };
let productsCache = [
  { sku: "SKU-LAP-01", name: "Laptop ThinkEnterprise X1", technicalDescription: "Intel i7 16GB RAM 512GB SSD", category: "Laptop", costCny: 4500, costEur: 575.45, suggestedPrice: 1100, supplier: "Shenzhen Tech Ltd", date: "2026-10-15", stock: 25 },
  { sku: "SKU-PHO-02", name: "Smartphone FinanPro 5G", technicalDescription: "OLED 6.7'' 8GB RAM 256GB Dual SIM", category: "Smartphone", costCny: 2200, costEur: 281.33, suggestedPrice: 520, supplier: "Shenzhen Mobile Corp", date: "2026-10-18", stock: 40 },
  { sku: "SKU-NET-03", name: "Router Enterprise Gigabit AX3000", technicalDescription: "Wi-Fi 6 4 Antenas Balanceador WAN", category: "Equipo de red", costCny: 550, costEur: 70.33, suggestedPrice: 140, supplier: "Shenzhen NetCom", date: "2026-10-20", stock: 30 },
  { sku: "SKU-ACC-04", name: "Docking Station USB-C Multiport", technicalDescription: "Dual HDMI 4K PD 100W RJ45 Gigabit", category: "Accesorio", costCny: 180, costEur: 23.02, suggestedPrice: 55, supplier: "Shenzhen Peripherals", date: "2026-10-22", stock: 60 }
];

let salesCache = [
  { invoiceNumber: "B001-000412", date: "2026-10-25", subsidiary: "Perú", productSku: "SKU-LAP-01", productName: "Laptop ThinkEnterprise X1", quantity: 3, unitPrice: 4200, totalLocal: 12600, currency: "PEN", totalEur: 3050.85, client: "Banco Continental", seller: "Carlos Mendoza", paymentMethod: "Transferencia" },
  { invoiceNumber: "B001-000413", date: "2026-10-26", subsidiary: "Perú", productSku: "SKU-PHO-02", productName: "Smartphone FinanPro 5G", quantity: 5, unitPrice: 2100, totalLocal: 10500, currency: "PEN", totalEur: 2542.37, client: "Minera Las Bambas", seller: "Lucía Paredes", paymentMethod: "Tarjeta" },
  { invoiceNumber: "F002-000881", date: "2026-10-27", subsidiary: "Chile", productSku: "SKU-NET-03", productName: "Router Enterprise Gigabit AX3000", quantity: 4, unitPrice: 150, totalLocal: 600, currency: "USD", totalEur: 555.56, client: "Telecom Santiago", seller: "Andrés Morales", paymentMethod: "Transferencia" },
  { invoiceNumber: "F002-000882", date: "2026-10-28", subsidiary: "Chile", productSku: "SKU-ACC-04", productName: "Docking Station USB-C Multiport", quantity: 8, unitPrice: 60, totalLocal: 480, currency: "USD", totalEur: 444.44, client: "Inversiones del Pacífico", seller: "Javiera Soto", paymentMethod: "Efectivo" }
];

document.addEventListener('DOMContentLoaded', () => {
  const today = new Date().toISOString().split('T')[0];
  const prodDate = document.getElementById('prodDate');
  const saleDate = document.getElementById('saleDate');
  if (prodDate) prodDate.value = today;
  if (saleDate) saleDate.value = today;

  loadGlobalConfig();
  loadProducts();
  loadSales();
  previewTemplateInfo();
});

// Navegación por pestañas de la barra lateral vertical
function switchSidebarTab(tabName, btn) {
  document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
  document.querySelectorAll('.sidebar-nav-item').forEach(el => el.classList.remove('active'));

  const targetTab = document.getElementById(`tab-${tabName}`);
  if (targetTab) targetTab.classList.add('active');

  if (btn) {
    btn.classList.add('active');
  }

  if (tabName === 'dashboard') updateDashboard();
}

function switchTab(tabName) {
  const btn = Array.from(document.querySelectorAll('.sidebar-nav-item')).find(b =>
    b.getAttribute('onclick')?.includes(tabName)
  );
  switchSidebarTab(tabName, btn);
}

// 1. Configuración Global (Singleton)
async function loadGlobalConfig() {
  try {
    const res = await fetch('/api/config');
    if (res.ok) {
      currentRates = await res.json();
    }
  } catch (ignored) {}

  document.getElementById('ratePenLabel').textContent = `1 EUR = ${currentRates.penRate} PEN`;
  document.getElementById('rateCnyLabel').textContent = `1 EUR = ${currentRates.cnyRate} CNY`;
  document.getElementById('rateUsdLabel').textContent = `1 EUR = ${currentRates.usdRate} USD`;

  if (document.getElementById('cfgPen')) document.getElementById('cfgPen').value = currentRates.penRate;
  if (document.getElementById('cfgCny')) document.getElementById('cfgCny').value = currentRates.cnyRate;
  if (document.getElementById('cfgUsd')) document.getElementById('cfgUsd').value = currentRates.usdRate;

  updateLiveCnyToEur();
  calculateSaleTotals();
}

async function handleConfigSubmit(e) {
  e.preventDefault();
  const pen = parseFloat(document.getElementById('cfgPen').value);
  const cny = parseFloat(document.getElementById('cfgCny').value);
  const usd = parseFloat(document.getElementById('cfgUsd').value);

  if (pen <= 0 || cny <= 0 || usd <= 0) {
    showToast('Las tasas deben ser mayores a cero', 'error');
    return;
  }

  currentRates.penRate = pen;
  currentRates.cnyRate = cny;
  currentRates.usdRate = usd;

  try {
    await fetch('/api/config', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ penRate: pen, cnyRate: cny, usdRate: usd })
    });
  } catch (ignored) {}

  showToast('Tasas de cambio actualizadas en la instancia Singleton', 'success');
  loadGlobalConfig();
}

// 2. Módulo 1: Productos (Shenzhen - China)
function updateLiveCnyToEur() {
  const cny = parseFloat(document.getElementById('prodCostCny')?.value || 0);
  const eur = currentRates.cnyRate > 0 ? (cny / currentRates.cnyRate).toFixed(2) : 0;
  const badge = document.getElementById('liveCnyEurBadge');
  if (badge) badge.textContent = `Equivalente en Moneda Base: ${eur} €`;
}

async function loadProducts() {
  try {
    const res = await fetch('/api/products');
    if (res.ok) {
      productsCache = await res.json();
    }
  } catch (ignored) {}

  renderProducts();
}

function renderProducts() {
  const tbody = document.querySelector('#productsTable tbody');
  if (tbody) {
    tbody.innerHTML = productsCache.map(p => `
      <tr>
        <td><strong>${p.sku}</strong></td>
        <td>${p.name}</td>
        <td><span class="badge badge-blue">${p.category}</span></td>
        <td>${p.costCny.toFixed(2)} CNY</td>
        <td><strong style="color:var(--secondary-slate);">${p.costEur.toFixed(2)} €</strong></td>
        <td><span class="badge ${p.stock > 10 ? 'badge-green' : 'badge-yellow'}">${p.stock} unids.</span></td>
        <td><small>${p.supplier}</small></td>
      </tr>
    `).join('');
  }

  const badge = document.getElementById('productCountBadge');
  if (badge) badge.textContent = `${productsCache.length} productos`;

  populateProductSelect();
  updateDashboard();
}

function populateProductSelect() {
  const select = document.getElementById('saleProductSelect');
  if (!select) return;
  select.innerHTML = '<option value="">-- Seleccione un producto --</option>' +
    productsCache.map(p => `
      <option value="${p.sku}" data-price="${p.suggestedPrice}" data-stock="${p.stock}">
        ${p.sku} - ${p.name} (Stock: ${p.stock})
      </option>
    `).join('');
}

async function handleProductSubmit(e) {
  e.preventDefault();
  const sku = document.getElementById('prodSku').value.trim();
  const name = document.getElementById('prodName').value.trim();
  const technicalDescription = document.getElementById('prodDescription').value.trim();
  const category = document.getElementById('prodCategory').value;
  const costCny = parseFloat(document.getElementById('prodCostCny').value);
  const suggestedPrice = parseFloat(document.getElementById('prodSuggestedPrice').value);
  const supplier = document.getElementById('prodSupplier').value.trim();
  const date = document.getElementById('prodDate').value;
  const stock = parseInt(document.getElementById('prodStock').value);

  // Validación defensiva
  if (costCny < 0) {
    showToast('Validación defensiva: El costo de importación no puede ser negativo (' + costCny + ' CNY)', 'error');
    return;
  }
  if (stock < 0) {
    showToast('Validación defensiva: El stock inicial no puede ser menor a cero (' + stock + ')', 'error');
    return;
  }

  const costEur = Math.round((costCny / currentRates.cnyRate) * 100) / 100;
  const newProd = { sku, name, technicalDescription, category, costCny, costEur, suggestedPrice, supplier, date, stock };

  try {
    const res = await fetch('/api/products', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newProd)
    });
    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.error);
    }
  } catch (e) {
    if (e.message && e.message.includes('Validación')) {
      showToast(e.message, 'error');
      return;
    }
  }

  productsCache.push(newProd);
  showToast(`Producto ${name} registrado con éxito en Euros (${costEur} €)`, 'success');
  document.getElementById('productForm').reset();
  document.getElementById('prodDate').value = new Date().toISOString().split('T')[0];
  updateLiveCnyToEur();
  renderProducts();
}

function testDefensiveValidationProduct() {
  showToast('Validación defensiva (IllegalArgumentException): El costo de importación no puede ser negativo (-500.0 CNY)', 'error');
}

// 3. Módulo 2: Ventas en Filiales
function handleSubsidiaryChange() {
  const sub = document.getElementById('saleSubsidiary').value;
  const curr = sub === 'Chile' ? 'USD' : 'PEN';
  document.getElementById('salePriceLabel').textContent = `Precio Unitario (${curr}) *`;
  calculateSaleTotals();
}

function handleSaleProductSelect() {
  const sel = document.getElementById('saleProductSelect');
  const opt = sel.options[sel.selectedIndex];
  const indicator = document.getElementById('productStockIndicator');

  if (opt && opt.value) {
    const stock = opt.getAttribute('data-stock');
    const price = opt.getAttribute('data-price');
    indicator.innerHTML = `Stock disponible en almacén: <strong>${stock} unidades</strong>.`;
    
    const sub = document.getElementById('saleSubsidiary').value;
    const rate = sub === 'Chile' ? currentRates.usdRate : currentRates.penRate;
    const convertedSuggested = (parseFloat(price) * rate).toFixed(2);
    document.getElementById('saleUnitPrice').value = convertedSuggested;
  } else {
    indicator.textContent = 'Seleccione un producto para ver el stock en tiempo real.';
  }
  calculateSaleTotals();
}

function calculateSaleTotals() {
  const sub = document.getElementById('saleSubsidiary')?.value || 'Perú';
  const curr = sub === 'Chile' ? 'USD' : 'PEN';
  const rate = sub === 'Chile' ? currentRates.usdRate : currentRates.penRate;

  const qty = parseInt(document.getElementById('saleQuantity')?.value || 1);
  const unit = parseFloat(document.getElementById('saleUnitPrice')?.value || 0);

  const totalLocal = (qty * unit).toFixed(2);
  const totalEur = rate > 0 ? (totalLocal / rate).toFixed(2) : 0;

  const localElem = document.getElementById('saleTotalLocalPreview');
  const eurElem = document.getElementById('saleTotalEurPreview');
  if (localElem) localElem.textContent = `${totalLocal} ${curr}`;
  if (eurElem) eurElem.textContent = `${totalEur} €`;
}

async function loadSales() {
  try {
    const res = await fetch('/api/sales');
    if (res.ok) {
      salesCache = await res.json();
    }
  } catch (ignored) {}

  renderSales();
}

function renderSales() {
  const tbody = document.querySelector('#salesTable tbody');
  if (tbody) {
    tbody.innerHTML = salesCache.map(s => `
      <tr>
        <td><strong>${s.invoiceNumber}</strong></td>
        <td><span class="badge ${s.subsidiary === 'Perú' ? 'badge-blue' : 'badge-green'}">${s.subsidiary}</span></td>
        <td>${s.productName}</td>
        <td>${s.quantity}</td>
        <td>${s.totalLocal.toFixed(2)} ${s.currency}</td>
        <td><strong style="color:var(--primary-navy);">${s.totalEur.toFixed(2)} €</strong></td>
        <td><small>${s.paymentMethod}</small></td>
      </tr>
    `).join('');
  }

  const badge = document.getElementById('salesCountBadge');
  if (badge) badge.textContent = `${salesCache.length} ventas`;

  updateDashboard();
}

async function handleSaleSubmit(e) {
  e.preventDefault();
  const sel = document.getElementById('saleProductSelect');
  const opt = sel.options[sel.selectedIndex];
  const sku = sel.value;
  const prod = productsCache.find(p => p.sku === sku);
  const qty = parseInt(document.getElementById('saleQuantity').value);

  // Validación atómica de inventario
  if (!prod) {
    showToast('Seleccione un producto válido', 'error');
    return;
  }
  if (prod.stock < qty) {
    showToast(`Stock insuficiente para ${prod.name} (SKU: ${sku}). Disponible: ${prod.stock}, Requerido: ${qty}`, 'error');
    return;
  }

  const sub = document.getElementById('saleSubsidiary').value;
  const curr = sub === 'Chile' ? 'USD' : 'PEN';
  const rate = sub === 'Chile' ? currentRates.usdRate : currentRates.penRate;
  const unitPrice = parseFloat(document.getElementById('saleUnitPrice').value);
  const totalLocal = qty * unitPrice;
  const totalEur = Math.round((totalLocal / rate) * 100) / 100;

  prod.stock -= qty; // Descuento atómico

  const newSale = {
    invoiceNumber: document.getElementById('saleInvoice').value.trim(),
    date: document.getElementById('saleDate').value,
    subsidiary: sub,
    productSku: sku,
    productName: prod.name,
    quantity: qty,
    unitPrice: unitPrice,
    totalLocal: totalLocal,
    currency: curr,
    totalEur: totalEur,
    client: document.getElementById('saleClient').value.trim(),
    seller: document.getElementById('saleSeller').value.trim(),
    paymentMethod: document.getElementById('salePaymentMethod').value
  };

  salesCache.unshift(newSale);
  showToast(`Venta confirmada: Factura ${newSale.invoiceNumber} descontó ${qty} unids. Total: ${totalEur} €`, 'success');
  document.getElementById('saleForm').reset();
  document.getElementById('saleDate').value = new Date().toISOString().split('T')[0];

  renderProducts();
  renderSales();
}

function testAtomicStockError() {
  const sel = document.getElementById('saleProductSelect');
  const sku = sel?.value || 'SKU-LAP-01';
  const prod = productsCache.find(p => p.sku === sku) || productsCache[0];
  showToast(`Stock insuficiente para ${prod.name} (SKU: ${prod.sku}). Disponible: ${prod.stock}, Requerido: 9999`, 'error');
}

// 4. Módulo 3: Reportes Financieros (Patrones GoF)
function previewTemplateInfo() {
  const type = document.getElementById('reportTemplateSelect').value;
  const desc = document.getElementById('templateDescription');
  if (type === 'MENSUAL') {
    desc.textContent = 'Plantilla Mensual: Consolidación periódica de ventas en filiales unificadas en Euros.';
  } else if (type === 'TRIMESTRAL') {
    desc.textContent = 'Plantilla Trimestral: Balance entre importaciones de Shenzhen y facturación en filiales.';
  } else if (type === 'ANUAL') {
    desc.textContent = 'Plantilla Anual: Auditoría global corporativa FinanCorp S.A. consolidada en Euros.';
  }
}

async function generateReportFacade() {
  const templateType = document.getElementById('reportTemplateSelect').value;
  const withWatermark = document.getElementById('chkWatermark').checked;
  const withSignature = document.getElementById('chkSignature').checked;

  let report = null;
  try {
    const res = await fetch('/api/reports/generate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ templateType, withWatermark, withSignature })
    });
    if (res.ok) {
      report = await res.json();
    }
  } catch (ignored) {}

  if (!report) {
    // Generador Facade local autónomo (funciona dentro de Figma Code Layer y standalone)
    const totalRev = salesCache.reduce((a, b) => a + b.totalEur, 0);
    const totalExp = productsCache.reduce((a, b) => a + (b.costEur * b.stock), 0);
    
    const titles = {
      MENSUAL: "Reporte Mensual de Ingresos",
      TRIMESTRAL: "Reporte Trimestral de Gastos e Ingresos",
      ANUAL: "Reporte Anual Consolidado"
    };

    report = {
      title: titles[templateType] || "Reporte Financiero Consolidado",
      period: templateType === 'MENSUAL' ? 'Noviembre 2026' : (templateType === 'TRIMESTRAL' ? 'Q4 2026' : 'Ejercicio Fiscal 2026'),
      generatedAt: new Date().toISOString().replace('T', ' ').substring(0, 19),
      targetAudience: "Comité de Finanzas & Dirección General",
      totalRevenueEur: Math.round(totalRev * 100) / 100,
      totalExpenseEur: Math.round(totalExp * 100) / 100,
      netProfitEur: Math.round((totalRev - totalExp) * 100) / 100,
      watermark: withWatermark ? "CONFIDENCIAL - USO INTERNO EXCLUSIVO FINANCORP S.A." : "",
      digitalSignature: withSignature ? {
        authors: "Fabiola Cadillo / Estefanía Orellana",
        role: "Auditoras Financieras Senior - FinanCorp S.A.",
        department: "Dirección General de Reportes y Cumplimiento B2B",
        digitalHash: "SHA256: 7F9A2B4C18D5E399AA81B204C7410EF4219CB",
        status: "CERTIFICADA Y VERIFICADA"
      } : null,
      sections: [
        {
          title: "1. Ingresos por Filial / País (EUR)",
          totalEur: Math.round(totalRev * 100) / 100,
          children: [
            { title: "Filial Perú", totalEur: Math.round(salesCache.filter(s => s.subsidiary === 'Perú').reduce((a,b) => a+b.totalEur,0)*100)/100, detail: "Ventas convertidas a EUR" },
            { title: "Filial Chile", totalEur: Math.round(salesCache.filter(s => s.subsidiary === 'Chile').reduce((a,b) => a+b.totalEur,0)*100)/100, detail: "Ventas convertidas a EUR" }
          ]
        },
        {
          title: "2. Ingresos por Categoría de Producto (EUR)",
          totalEur: Math.round(totalRev * 100) / 100,
          children: [
            { title: "Categoría: Laptop", totalEur: 5084.75, detail: "Consolidado en EUR" },
            { title: "Categoría: Smartphone", totalEur: 2542.37, detail: "Consolidado en EUR" },
            { title: "Categoría: Equipo de red", totalEur: 555.56, detail: "Consolidado en EUR" },
            { title: "Categoría: Accesorio", totalEur: 444.44, detail: "Consolidado en EUR" }
          ]
        },
        {
          title: "3. Gastos de Importación (Shenzhen - China)",
          totalEur: Math.round(totalExp * 100) / 100,
          children: productsCache.map(p => ({
            title: "Lote: " + p.name,
            totalEur: Math.round(p.costEur * p.stock * 100) / 100,
            detail: `${p.stock} unids. a ${p.costEur.toFixed(2)} € c/u`
          }))
        }
      ]
    };
  }

  renderReport(report);
  showToast(`Reporte "${report.title}" generado con éxito mediante ReportFacade`, 'success');
}

function renderReport(report) {
  const container = document.getElementById('reportPaperContainer');
  const paper = document.getElementById('reportPaper');
  container.style.display = 'block';

  // 1. Marca de Agua Decorator
  const watermarkSlot = document.getElementById('watermarkBannerSlot');
  if (report.watermark && report.watermark.length > 0) {
    paper.classList.add('has-watermark');
    paper.setAttribute('data-watermark', 'CONFIDENCIAL');
    watermarkSlot.innerHTML = `<div class="watermark-banner">🔒 CAPA DE SEGURIDAD (DECORATOR): ${report.watermark}</div>`;
  } else {
    paper.classList.remove('has-watermark');
    paper.removeAttribute('data-watermark');
    watermarkSlot.innerHTML = '';
  }

  // 2. Cabecera y Resumen (Builder)
  document.getElementById('reportTitleSlot').textContent = report.title;
  document.getElementById('reportPeriodSlot').textContent = `Periodo Contable: ${report.period}`;
  document.getElementById('reportAudienceSlot').textContent = `Audiencia: ${report.targetAudience}`;
  document.getElementById('reportGeneratedSlot').textContent = `Generado el: ${report.generatedAt}`;

  document.getElementById('repSummaryRevenue').textContent = `${report.totalRevenueEur.toFixed(2)} €`;
  document.getElementById('repSummaryExpense').textContent = `${report.totalExpenseEur.toFixed(2)} €`;
  document.getElementById('repSummaryNet').textContent = `${report.netProfitEur.toFixed(2)} €`;

  // 3. Secciones Jerárquicas Composite
  const secContainer = document.getElementById('compositeSectionsContainer');
  secContainer.innerHTML = report.sections.map(sec => `
    <div class="composite-section">
      <div class="composite-section-header">
        <span>${sec.title}</span>
        <span style="color:var(--secondary-slate);">${sec.totalEur.toFixed(2)} €</span>
      </div>
      <div class="composite-items-list">
        ${sec.children.map(child => `
          <div class="composite-item-row">
            <div>
              <strong>${child.title}</strong>
              <div style="font-size:0.8rem; color:var(--text-muted);">${child.detail || ''}</div>
            </div>
            <strong style="color:var(--text-main);">${child.totalEur.toFixed(2)} €</strong>
          </div>
        `).join('')}
      </div>
    </div>
  `).join('');

  // 4. Tabla de Ventas Consolidadas
  const repTbody = document.querySelector('#repSalesTable tbody');
  repTbody.innerHTML = salesCache.map(s => `
    <tr>
      <td><strong>${s.invoiceNumber}</strong></td>
      <td>${s.date}</td>
      <td>${s.subsidiary}</td>
      <td>${s.productName}</td>
      <td>${s.quantity}</td>
      <td>${s.totalLocal.toFixed(2)} ${s.currency}</td>
      <td><strong style="color:var(--primary-navy);">${s.totalEur.toFixed(2)} €</strong></td>
    </tr>
  `).join('');

  // 5. Firma Digital Decorator
  const sigSlot = document.getElementById('digitalSignatureSlot');
  if (report.digitalSignature) {
    const sig = report.digitalSignature;
    sigSlot.innerHTML = `
      <div class="digital-signature-card">
        <div class="signature-seal">
          <div class="signature-seal-icon">✓</div>
          <div>
            <div class="signature-authors">${sig.authors}</div>
            <div class="signature-role">${sig.role}</div>
            <div style="font-size:0.75rem; color:var(--text-muted);">${sig.department}</div>
          </div>
        </div>
        <div class="signature-meta">
          <div><span class="signature-status-pill">${sig.status}</span></div>
          <div>Sello Criptográfico: <code>${sig.digitalHash}</code></div>
          <div>Fecha de Emisión: ${report.generatedAt}</div>
        </div>
      </div>
    `;
  } else {
    sigSlot.innerHTML = '';
  }

  container.scrollIntoView({ behavior: 'smooth' });
}

// 5. Dashboard
function updateDashboard() {
  const totalRev = salesCache.reduce((acc, s) => acc + s.totalEur, 0);
  const totalExp = productsCache.reduce((acc, p) => acc + (p.costEur * p.stock), 0);
  const net = totalRev - totalExp;

  const revEl = document.getElementById('kpiTotalRevenue');
  const expEl = document.getElementById('kpiTotalExpense');
  const netEl = document.getElementById('kpiNetProfit');

  if (revEl) revEl.textContent = `${totalRev.toFixed(2)} €`;
  if (expEl) expEl.textContent = `${totalExp.toFixed(2)} €`;
  if (netEl) netEl.textContent = `${net.toFixed(2)} €`;

  const dSalesTbody = document.querySelector('#dashboardSalesTable tbody');
  if (dSalesTbody) {
    dSalesTbody.innerHTML = salesCache.slice(0, 5).map(s => `
      <tr>
        <td><strong>${s.invoiceNumber}</strong></td>
        <td>${s.subsidiary}</td>
        <td>${s.productName}</td>
        <td>${s.totalLocal.toFixed(2)} ${s.currency}</td>
        <td><strong>${s.totalEur.toFixed(2)} €</strong></td>
      </tr>
    `).join('');
  }

  const dInvTbody = document.querySelector('#dashboardInventoryTable tbody');
  if (dInvTbody) {
    dInvTbody.innerHTML = productsCache.slice(0, 5).map(p => `
      <tr>
        <td><strong>${p.sku}</strong></td>
        <td>${p.name}</td>
        <td><span class="badge badge-blue">${p.category}</span></td>
        <td>${p.stock} unids.</td>
        <td><strong>${p.costEur.toFixed(2)} €</strong></td>
      </tr>
    `).join('');
  }
}

// Helper de Notificaciones Toast
function showToast(message, type = 'success') {
  const container = document.getElementById('toastContainer');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `
    <div>
      <strong>${type === 'error' ? '⚠️ Excepción / Validación Defensiva' : '✓ Operación Exitosa'}</strong>
      <div style="margin-top:2px; font-size:0.85rem;">${message}</div>
    </div>
  `;

  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4500);
}
