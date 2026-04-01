package com.example.dailyfieldreport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {
    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());
    private static final Gson GSON = new Gson();

    public static void main(String[] args) throws Exception {
        startServer(8080);
    }

    public static void startServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticHandler());
        server.createContext("/submit", new SubmitHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        LOGGER.info("Web server started on http://localhost:" + port);
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) {
                try (InputStream is = WebServer.class.getResourceAsStream("/web/index.html")) {
                    if (is == null) {
                        byte[] msg = "Index page not found".getBytes(StandardCharsets.UTF_8);
                        exchange.sendResponseHeaders(500, msg.length);
                        exchange.getResponseBody().write(msg);
                        exchange.close();
                        return;
                    }
                    byte[] bytes = is.readAllBytes();
                    Headers h = exchange.getResponseHeaders();
                    h.set("Content-Type", "text/html; charset=utf-8");
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                } catch (Throwable t) {
                    LOGGER.log(Level.SEVERE, "Error serving index.html", t);
                    byte[] msg = "Server error".getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(500, msg.length);
                    exchange.getResponseBody().write(msg);
                } finally {
                    exchange.close();
                }
            } else {
                byte[] msg = "Not found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, msg.length);
                exchange.getResponseBody().write(msg);
                exchange.close();
            }
        }
    }

    static class SubmitHandler implements HttpHandler {
        static class Submission {
            Map<String, String> fields;
            List<String> photosBase64;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            Headers reqHeaders = exchange.getRequestHeaders();
            String contentType = reqHeaders.getFirst("Content-Type");
            if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
                exchange.sendResponseHeaders(415, -1);
                exchange.close();
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Type t = new TypeToken<Submission>() {}.getType();
            Submission sub;
            try {
                sub = GSON.fromJson(body, t);
            } catch (Throwable parseEx) {
                LOGGER.log(Level.WARNING, "Failed to parse submission JSON", parseEx);
                exchange.sendResponseHeaders(400, -1);
                exchange.close();
                return;
            }

            // Build textual content similar to collectFormData
            StringBuilder sb = new StringBuilder();
            sb.append("DAILY FIELD REPORT\n\n");
            Map<String,String> f = sub.fields == null ? Collections.emptyMap() : sub.fields;
            sb.append("Project Name: ").append(f.getOrDefault("projectName","")).append("\n");
            sb.append("Project No.: ").append(f.getOrDefault("projectNo","")).append("\n");
            sb.append("Location: ").append(f.getOrDefault("location","")).append("\n");
            sb.append("Date: ").append(f.getOrDefault("date","")).append("\n");
            sb.append("Weather: ").append(f.getOrDefault("weather","")).append("\n");
            sb.append("Temperature: ").append(f.getOrDefault("temperature","")).append("\n");
            sb.append("Wind: ").append(f.getOrDefault("wind","")).append("\n\n");
            sb.append("WORK PERFORMED:\n").append(f.getOrDefault("workPerformed","")).append("\n\n");
            sb.append("SAFETY:\n");
            sb.append("Meeting Held: ").append(Boolean.parseBoolean(f.getOrDefault("safetyMeeting","false"))?"Yes":"No").append("\n");
            sb.append("Topic: ").append(f.getOrDefault("safetyTopic","")).append("\n");
            sb.append("Attendees: ").append(f.getOrDefault("safetyAttendees","")).append("\n");
            sb.append("Incidents/Near Misses: ").append(Boolean.parseBoolean(f.getOrDefault("safetyIncident","false"))?"Yes":"No").append("\n\n");
            sb.append("COORDINATION:\n");
            sb.append("Visitors: ").append(f.getOrDefault("visitors","")).append("\n");
            sb.append("Meetings: ").append(f.getOrDefault("meetings","")).append("\n");
            sb.append("Directions: ").append(f.getOrDefault("directions","")).append("\n\n");
            sb.append("ATTACHMENTS:\n");
            if (sub.photosBase64 != null && !sub.photosBase64.isEmpty()) sb.append(" - Photos attached: ").append(sub.photosBase64.size()).append("\n");

            // Decode photos to temp files
            List<Path> tempPhotoPaths = new ArrayList<>();
            try {
                if (sub.photosBase64 != null) {
                    int i = 0;
                    for (String b64 : sub.photosBase64) {
                        if (b64 == null || b64.isEmpty()) continue;
                        String clean = b64;
                        int comma = clean.indexOf(',');
                        if (comma >= 0) clean = clean.substring(comma + 1);
                        byte[] bytes = Base64.getDecoder().decode(clean);
                        Path tmp = Files.createTempFile("dfr-photo-" + (i++), ".png");
                        Files.write(tmp, bytes);
                        tempPhotoPaths.add(tmp);
                    }
                }

                // Generate PDF using text content and attached photos
                Path out = Files.createTempFile("dfr-report-",".pdf");

                try {
                    ReportExporter.saveTextAsPDF(sb.toString(), tempPhotoPaths, out.toAbsolutePath().toString());

                    // Respond with PDF bytes for direct download
                    byte[] pdfBytes = Files.readAllBytes(out);
                    Headers rh = exchange.getResponseHeaders();
                    rh.set("Content-Type", "application/pdf");
                    rh.set("Content-Disposition", "attachment; filename=DailyFieldReport.pdf");
                    exchange.sendResponseHeaders(200, pdfBytes.length);
                    exchange.getResponseBody().write(pdfBytes);
                } finally {
                    // cleanup generated file
                    try { Files.deleteIfExists(out); } catch (Throwable ignored) {}
                }
            } catch (Throwable t) {
                LOGGER.log(Level.SEVERE, "Error generating report", t);
                byte[] msg = "Server error generating report".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, msg.length);
                exchange.getResponseBody().write(msg);
            } finally {
                // cleanup temp photos
                for (Path pth : tempPhotoPaths) {
                    try { Files.deleteIfExists(pth); } catch (Throwable ignored) {}
                }
                exchange.close();
            }
        }
    }
}

