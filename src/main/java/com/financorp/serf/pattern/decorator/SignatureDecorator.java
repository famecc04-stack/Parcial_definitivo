package com.financorp.serf.pattern.decorator;

import com.financorp.serf.pattern.builder.FinancialReport;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Patrón Decorator Concreto: Agrega la tarjeta de Firma Digital corporativa.
 * Autoras oficiales: Fabiola Cadillo / Estefanía Orellana
 */
public class SignatureDecorator extends ReportDecorator {

    public SignatureDecorator(FinancialReport report) {
        super(report);
    }

    @Override
    public void decorate() {
        Map<String, String> signature = new LinkedHashMap<>();
        signature.put("authors", "Fabiola Cadillo / Estefanía Orellana");
        signature.put("role", "Auditoras Financieras Senior - FinanCorp S.A.");
        signature.put("department", "Dirección General de Reportes y Cumplimiento B2B");
        signature.put("digitalHash", "SHA256: 7F9A2B4C18D5E399AA81B204C7410EF4219CB");
        signature.put("status", "CERTIFICADA Y VERIFICADA");
        wrappedReport.setDigitalSignature(signature);
    }
}
