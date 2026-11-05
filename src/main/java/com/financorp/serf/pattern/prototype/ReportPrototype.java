package com.financorp.serf.pattern.prototype;

/**
 * Patrón Prototype: Define la interfaz para clonar plantillas de reportes financieros.
 */
public interface ReportPrototype extends Cloneable {
    ReportPrototype cloneReport();
}
