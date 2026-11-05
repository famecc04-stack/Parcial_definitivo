package com.financorp.serf.pattern.composite;

import java.util.*;

/**
 * Patrón Composite: Nodo compuesto que agrupa subsecciones y elementos financieros.
 */
public class ReportSection implements ReportComponent {
    private final String title;
    private final List<ReportComponent> children = new ArrayList<>();

    public ReportSection(String title) {
        this.title = title;
    }

    public void add(ReportComponent component) {
        this.children.add(component);
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public double getTotalEur() {
        double total = 0.0;
        for (ReportComponent child : children) {
            total += child.getTotalEur();
        }
        return total;
    }

    public List<ReportComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", "section");
        map.put("title", title);
        map.put("totalEur", Math.round(getTotalEur() * 100.0) / 100.0);
        List<Map<String, Object>> childMaps = new ArrayList<>();
        for (ReportComponent c : children) {
            childMaps.add(c.toMap());
        }
        map.put("children", childMaps);
        return map;
    }
}
