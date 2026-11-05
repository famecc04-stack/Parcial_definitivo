package com.financorp.serf;

import com.financorp.serf.web.ApiHandler;
import com.financorp.serf.web.StaticFileHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Clase principal de inicio de la aplicación corporativa SERF - FinanCorp S.A.
 */
public class App {
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Directorio base para frontend en tiempo de desarrollo o ejecución
        String staticDir = "src/main/resources/static";
        if (!new File(staticDir).exists()) {
            staticDir = "static";
        }

        // Registro de manejadores de rutas
        server.createContext("/api", new ApiHandler());
        server.createContext("/", new StaticFileHandler(staticDir));

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("=============================================================");
        System.out.println("  SERF - Sistema Empresarial de Gestion de Reportes Financieros");
        System.out.println("  FinanCorp S.A. - Moneda Base: Euros (EUR)");
        System.out.println("=============================================================");
        System.out.println("  Servidor iniciado con exito en:");
        System.out.println("  --> http://localhost:" + PORT);
        System.out.println("=============================================================");
    }
}
