package com.financorp.serf.web;

import com.financorp.serf.config.CorporateConfig;
import com.financorp.serf.pattern.prototype.ReportTemplate;
import com.financorp.serf.repository.DataStore;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Controlador API REST para el enrutamiento de peticiones de SERF.
 */
public class ApiHandler implements HttpHandler {
    private final DataStore dataStore = DataStore.getInstance();

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod().toUpperCase();

        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(method)) { ex.sendResponseHeaders(204, -1); return; }

        try {
            if ("/api/config".equals(path) && "GET".equals(method)) {
                send(ex, 200, JsonUtil.toJson(DtoMapper.configToMap(CorporateConfig.getInstance())));
            } else if ("/api/config".equals(path) && "POST".equals(method)) {
                send(ex, 200, ApiOperations.updateConfig(read(ex)));
            } else if ("/api/products".equals(path) && "GET".equals(method)) {
                send(ex, 200, JsonUtil.toJson(dataStore.getAllProducts().stream().map(DtoMapper::productToMap).collect(Collectors.toList())));
            } else if ("/api/products".equals(path) && "POST".equals(method)) {
                send(ex, 201, ApiOperations.createProduct(read(ex)));
            } else if ("/api/sales".equals(path) && "GET".equals(method)) {
                send(ex, 200, JsonUtil.toJson(dataStore.getAllSales().stream().map(DtoMapper::saleToMap).collect(Collectors.toList())));
            } else if ("/api/sales".equals(path) && "POST".equals(method)) {
                send(ex, 201, ApiOperations.createSale(read(ex)));
            } else if ("/api/templates".equals(path) && "GET".equals(method)) {
                send(ex, 200, JsonUtil.toJson(ReportTemplate.getAvailablePrototypes().values()));
            } else if ("/api/reports/generate".equals(path) && "POST".equals(method)) {
                send(ex, 200, ApiOperations.generateReport(read(ex)));
            } else {
                send(ex, 404, JsonUtil.errorJson("Ruta API no encontrada: " + path));
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            send(ex, 400, JsonUtil.errorJson(e.getMessage()));
        } catch (Exception e) {
            send(ex, 500, JsonUtil.errorJson("Error interno: " + e.getMessage()));
        }
    }

    private String read(HttpExchange ex) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8))) {
            return r.lines().collect(Collectors.joining("\n"));
        }
    }

    private void send(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}
