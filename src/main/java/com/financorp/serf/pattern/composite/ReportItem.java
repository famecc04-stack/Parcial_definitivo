package com.financorp.serf.pattern.composite;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Patrón Composite: Hoja (Leaf) que representa un registro financiero individual.
 */
public class ReportItem implements ReportComponent {
    private final String title;
    private final double totalEur;
    private final String detail;

    public ReportItem(String title, double totalEur, String detail) {
        this.title = title;
        this.totalEur = totalEur;
        this.detail = detail;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public double getTotalEur() { return totalEur; }

    public String getDetail() { return detail; }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "item");
        map.put("title", title);
        map.put("totalEur", Math.round(totalEur * 100.0) / 100.0);
        map.put("detail", detail);
        return map;
    }
}
