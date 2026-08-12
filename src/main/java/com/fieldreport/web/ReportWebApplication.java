package com.fieldreport.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fieldreport.service.FieldReportService;
import com.fieldreport.service.ReportExportService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Simple HTTP server for Daily Field Report download center.
 * 
 * This application provides a web-based interface for generating and downloading
 * field reports using Java's built-in HTTP server.
 * 
 * @author Daily Field Report System
 * @version 1.0.0
 */
public class ReportWebApplication {
    
    private static final int DEFAULT_PORT = 8080;
    
    private final HttpServer server;
    private final ReportWebController controller;
    private final int port;
    
    /**
     * Initialize the web application.
     */
    public ReportWebApplication() throws IOException {
        this(DEFAULT_PORT);
    }
    
    /**
     * Initialize the web application with specified port.
     */
    public ReportWebApplication(int port) throws IOException {
        this.port = port;
        
        // Initialize services
        FieldReportService fieldReportService = new FieldReportService();
        ReportExportService exportService = new ReportExportService();
        
        // Initialize controller
        this.controller = new ReportWebController(fieldReportService, exportService);
        
        // Create HTTP server
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Configure routes
        configureRoutes();
        
        System.out.println("Daily Field Report web application initialized on port " + port);
    }
    
    /**
     * Configure HTTP routes.
     */
    private void configureRoutes() {
        server.createContext("/", new MainPageHandler());
        server.createContext("/api/reports/generate-html", new HTMLReportHandler());
        server.createContext("/api/reports/generate-text", new TextReportHandler());
        server.createContext("/api/reports/sample-html", new SampleHTMLHandler());
        server.createContext("/api/reports/sample-text", new SampleTextHandler());
        server.createContext("/api/health", new HealthHandler());
    }
    
    /**
     * Start the web server.
     */
    public void start() {
        server.setExecutor(null);
        server.start();
        
        System.out.println("🌐 Daily Field Report web server started successfully!");
        System.out.println("📋 Access the interface at: http://localhost:" + port);
        System.out.println("🔗 API endpoints:");
        System.out.println("   - Main interface: http://localhost:" + port + "/");
        System.out.println("   - Sample HTML report: http://localhost:" + port + "/api/reports/sample-html");
        System.out.println("   - Sample text report: http://localhost:" + port + "/api/reports/sample-text");
        System.out.println("   - Health check: http://localhost:" + port + "/api/health");
        System.out.println("📁 Generated reports will be saved to: ./exports/");
        System.out.println("ℹ️  Press Ctrl+C to stop the server");
    }
    
    /**
     * Start the web server with a specific port.
     */
    public void start(int requestedPort) {
        if (requestedPort != this.port) {
            System.out.println("⚠️  Server was initialized for port " + this.port + ", but requested port " + requestedPort);
            System.out.println("🔄 Using initialized port " + this.port);
        }
        start();
    }
    
    /**
     * Stop the web server.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Daily Field Report web server stopped");
        }
    }
    
    /**
     * Parse form data from POST request.
     */
    private Map<String, String> parseFormData(String formData) {
        Map<String, String> data = new HashMap<>();
        if (formData != null && !formData.isEmpty()) {
            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                        String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                        data.put(key, value);
                    } catch (Exception e) {
                        System.err.println("Error parsing form data: " + e.getMessage());
                    }
                }
            }
        }
        return data;
    }
    
    /**
     * Send HTTP response.
     */
    private void sendResponse(HttpExchange exchange, int statusCode, String contentType, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    /**
     * Send file download response.
     */
    private void sendFileResponse(HttpExchange exchange, String filePath, String contentType, String filename) throws IOException {
        try {
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                sendResponse(exchange, 404, "text/plain", "File not found");
                return;
            }
            
            byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            exchange.sendResponseHeaders(200, fileContent.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileContent);
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "text/plain", "Error serving file: " + e.getMessage());
        }
    }
    
    // HTTP Handlers
    
    private class MainPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String html = controller.getMainPageHTML();
                sendResponse(exchange, 200, "text/html", html);
            } else {
                sendResponse(exchange, 405, "text/plain", "Method not allowed");
            }
        }
    }
    
    private class HTMLReportHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String formData = new String(exchange.getRequestBody().readAllBytes());
                    Map<String, String> data = parseFormData(formData);
                    
                    String filePath = controller.generateHTMLReport(data);
                    String filename = "FieldReport_" + data.getOrDefault("date", "today") + ".html";
                    
                    sendFileResponse(exchange, filePath, "text/html", filename);
                } catch (Exception e) {
                    sendResponse(exchange, 500, "text/plain", "Error generating report: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "text/plain", "Method not allowed");
            }
        }
    }
    
    private class TextReportHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    String formData = new String(exchange.getRequestBody().readAllBytes());
                    Map<String, String> data = parseFormData(formData);
                    
                    String filePath = controller.generateTextReport(data);
                    String filename = "FieldReport_" + data.getOrDefault("date", "today") + ".txt";
                    
                    sendFileResponse(exchange, filePath, "text/plain", filename);
                } catch (Exception e) {
                    sendResponse(exchange, 500, "text/plain", "Error generating report: " + e.getMessage());
                }
            } else {
                sendResponse(exchange, 405, "text/plain", "Method not allowed");
            }
        }
    }
    
    private class SampleHTMLHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String filePath = controller.generateSampleHTMLReport();
                sendFileResponse(exchange, filePath, "text/html", "Sample_FieldReport.html");
            } catch (Exception e) {
                sendResponse(exchange, 500, "text/plain", "Error generating sample report: " + e.getMessage());
            }
        }
    }
    
    private class SampleTextHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String filePath = controller.generateSampleTextReport();
                sendFileResponse(exchange, filePath, "text/plain", "Sample_FieldReport.txt");
            } catch (Exception e) {
                sendResponse(exchange, 500, "text/plain", "Error generating sample report: " + e.getMessage());
            }
        }
    }
    
    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\":\"healthy\",\"service\":\"Daily Field Report\"}";
            sendResponse(exchange, 200, "application/json", response);
        }
    }
    
    /**
     * Main method to run the web application standalone.
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting Daily Field Report Web Application...");
        
        try {
            ReportWebApplication webApp = new ReportWebApplication();
            
            // Add shutdown hook for graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("🛑 Shutting down Daily Field Report web server...");
                webApp.stop();
                System.out.println("✅ Web server shutdown complete");
            }));
            
            // Start the server
            webApp.start();
            
        } catch (IOException e) {
            System.err.println("❌ Failed to start web server: " + e.getMessage());
        }
    }
}