package com.financorp.serf.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servidor de archivos estáticos HTML5, CSS y JavaScript para el frontend.
 */
public class StaticFileHandler implements HttpHandler {
    private final String baseDir;

    public StaticFileHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uriPath = exchange.getRequestURI().getPath();
        if (uriPath.equals("/") || uriPath.isEmpty()) {
            uriPath = "/index.html";
        }

        byte[] content = loadContent(uriPath);
        if (content == null) {
            String notFound = "404 Not Found: " + uriPath;
            exchange.sendResponseHeaders(404, notFound.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(notFound.getBytes()); }
            return;
        }

        String mime = getMimeType(uriPath);
        exchange.getResponseHeaders().set("Content-Type", mime + "; charset=UTF-8");
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(content);
        }
    }

    private byte[] loadContent(String path) {
        try {
            // Intentar desde classpath
            InputStream is = getClass().getResourceAsStream("/static" + path);
            if (is != null) {
                return is.readAllBytes();
            }
            // Fallback a sistema de archivos
            Path filePath = Paths.get(baseDir, path);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                return Files.readAllBytes(filePath);
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String getMimeType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "text/plain";
    }
}
